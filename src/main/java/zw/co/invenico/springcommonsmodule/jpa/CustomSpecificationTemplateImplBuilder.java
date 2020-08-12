package zw.co.invenico.springcommonsmodule.jpa;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CustomSpecificationTemplateImplBuilder<T> {
    private static final Logger log = LoggerFactory.getLogger(CustomSpecificationTemplateImplBuilder.class);
    private final List<SearchCriteria> searchCriterias = new ArrayList<>();

    public CustomSpecificationTemplateImplBuilder() {
    }

    private void withSearchCriteria(String key, Object value, String operation) {
        this.searchCriterias.add(SearchCriteria.createSearchCriteria(key, operation, value));
    }

    private Specification<T> build() {
        if (this.searchCriterias.isEmpty()) {
            return null;
        } else {
            List<Specification<T>> specifications = new ArrayList<>();
            this.searchCriterias.forEach((searchCriteria) -> {
                specifications.add(new CustomSpecificationTemplateImpl<T>(searchCriteria));
            });
            Specification<T> result = specifications.get(0);

            for(int i = 1; i < specifications.size(); ++i) {
                result = Specification.where(result).and(specifications.get(i));
            }

            return result;
        }
    }

    public Specification<T> buildSpecification(String searchQuery) {
        log.info("------> Search query : {}", searchQuery);
        CustomSpecificationTemplateImplBuilder<T> builder = new CustomSpecificationTemplateImplBuilder<>();
        Pattern pattern = Pattern.compile("(\\w.+?)([:<>])((\\w+[\\s\\w]*)+?)");
        Matcher matcher = pattern.matcher(searchQuery + ",");

        while(matcher.find()) {
            builder.withSearchCriteria(matcher.group(1), matcher.group(3), matcher.group(2));
        }

        return builder.build();
    }
}
