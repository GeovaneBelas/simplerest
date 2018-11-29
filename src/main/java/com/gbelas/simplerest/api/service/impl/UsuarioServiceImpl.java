package com.gbelas.simplerest.api.service.impl;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.persistence.Column;
import javax.persistence.EntityManager;
import javax.persistence.Id;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Component;

import com.gbelas.simplerest.api.model.Usuario;
import com.gbelas.simplerest.api.repository.UsuarioRepository;
import com.gbelas.simplerest.api.service.UsuarioService;

@Component
public class UsuarioServiceImpl implements UsuarioService {

	@Autowired
	private UsuarioRepository usuarioRepository;

	@Autowired
	EntityManager em;

	public Usuario findByLogin(String login) {
		return this.usuarioRepository.findByLogin(login);
	}

	public Usuario createOrUpdate(Usuario user) {
		return this.usuarioRepository.save(user);
	}

	public Optional<Usuario> findById(Long id) {
		return this.usuarioRepository.findById(id);
	}

	public void delete(Long id) {
		this.usuarioRepository.deleteById(id);
	}

	public Page<Usuario> findAll(String search, int page, int limit, Direction direction, String propertie) {
		Pageable pages;

		if (direction == null || propertie == null) {
			pages = PageRequest.of(page, limit);
		} else {
			pages = PageRequest.of(page, limit, direction, propertie);
		}

		if (search == null) {
			return this.usuarioRepository.findAll(pages);
		} else {
			CriteriaBuilder cb = em.getCriteriaBuilder();
			CriteriaQuery<Usuario> cq = cb.createQuery(Usuario.class);

			Root<Usuario> r = cq.from(Usuario.class);

			long total = 0;
			{
				CriteriaQuery<Long> cqCount = cb.createQuery(Long.class);
				{
					List<Predicate> p = new ArrayList<>();

					Root<Usuario> rCount = cqCount.from(Usuario.class);
					List<Predicate> pCount = new ArrayList<>();

					for (Field f : Usuario.class.getDeclaredFields()) {
						if (f.isAnnotationPresent(Column.class) || f.isAnnotationPresent(Id.class)) {
							if (f.getType().isAssignableFrom(String.class)) {
								p.add(cb.like(cb.upper(r.get(f.getName())), "%" + search.toUpperCase() + "%"));
								pCount.add(
										cb.like(cb.upper(rCount.get(f.getName())), "%" + search.toUpperCase() + "%"));
							} else {
								p.add(cb.like(cb.upper(cb.concat(r.get(f.getName()), "")),
										"%" + search.toUpperCase() + "%"));
								pCount.add(cb.like(cb.upper(cb.concat(rCount.get(f.getName()), "")),
										"%" + search.toUpperCase() + "%"));
							}
						}
					}

					{
						cqCount.select(cb.count(rCount));
						cqCount.where(cb.or(pCount.toArray(new Predicate[0])));
						total = em.createQuery(cqCount).getSingleResult();
					}
					cq.where(cb.or(p.toArray(new Predicate[0])));

				}

				if (direction != null && propertie != null) {
					cq.orderBy(direction.equals(Direction.ASC) ? cb.asc(r.get(propertie)) : cb.desc(r.get(propertie)));
				}

			}
			TypedQuery<Usuario> query = em.createQuery(cq);

			query.setFirstResult(pages.getPageNumber() * pages.getPageSize());
			query.setMaxResults(pages.getPageSize());

			return new PageImpl<Usuario>(query.getResultList(), pages, total);
		}
	}
}
