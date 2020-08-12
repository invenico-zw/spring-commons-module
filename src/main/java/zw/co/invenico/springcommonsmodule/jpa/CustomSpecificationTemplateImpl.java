package zw.co.invenico.springcommonsmodule.jpa;

import lombok.val;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.*;
import java.util.Arrays;
import java.util.regex.Pattern;

public class CustomSpecificationTemplateImpl<T> implements Specification<T> {
    private static final Logger log = LoggerFactory.getLogger(CustomSpecificationTemplateImpl.class);
    private final  SearchCriteria searchCriteria;

    CustomSpecificationTemplateImpl( SearchCriteria searchCriteria) {
        this.searchCriteria = searchCriteria;
    }

    public Predicate toPredicate(Root<T> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
        val keys = searchCriteria.getKey().split(Pattern.quote("."));

        if (searchCriteria.getOperation().equalsIgnoreCase(Operations.GREATER_THAN.sign)) {

            return builder.greaterThanOrEqualTo(getRoot(searchCriteria.getKey(), root, keys),
                    searchCriteria.getValue().toString());

        } else if (searchCriteria.getOperation().equalsIgnoreCase(Operations.LESS_THAN.sign)) {

            return builder.lessThanOrEqualTo(getRoot(searchCriteria.getKey(), root,
                    keys), searchCriteria.getValue().toString());

        } else if (searchCriteria.getOperation().equalsIgnoreCase(Operations.EQUALS.sign)) {

            if (getRoot(searchCriteria.getKey(), root, keys).getJavaType().equals(String.class)) {

                return builder.like(getRoot(searchCriteria.getKey(), root, keys), "%" + searchCriteria.getValue() + "%");
//                return builder.equal(getRoot(searchCriteria.getKey(), root, keys),  searchCriteria.getValue());

            } else {

                return builder.equal(getRoot(searchCriteria.getKey(), root, keys), searchCriteria.getValue());

            }
        }

        return null;
    }

    private Expression getRoot(String key, Root<T> root, String... keys) {
        Arrays.asList(keys).forEach((key1) -> {
            log.info("---> {}", key1);
        });
        if (keys.length <= 1) {
            return root.get(key);
        } else {
            Path<Object> newRoot = root.get(keys[0]);

            for (int i = 1; i < keys.length - 1; ++i) {
                newRoot = newRoot.get(keys[i]);
            }

            return newRoot.get(keys[keys.length - 1]);
        }
    }
}
