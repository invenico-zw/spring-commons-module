package zw.co.invenico.springcommonsmodule.jpa;

import java.io.Serializable;

public class SearchCriteria implements Serializable {
    private final String key;
    private final String operation;
    private final Object value;

    private SearchCriteria(String key, String operation, Object value) {
        this.key = key;
        this.operation = operation;
        this.value = value;
    }

    static SearchCriteria createSearchCriteria(String key, String operation, Object value) {
        return new SearchCriteria(key, operation, value);
    }

    private SearchCriteria() {
        this.key = null;
        this.operation = null;
        this.value = null;
    }

    public String getKey() {
        return this.key;
    }

    public String getOperation() {
        return this.operation;
    }

    public Object getValue() {
        return this.value;
    }

    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        } else if (!(o instanceof SearchCriteria)) {
            return false;
        } else {
            SearchCriteria other = (SearchCriteria)o;
            if (!other.canEqual(this)) {
                return false;
            } else {
                label47: {
                    Object this$key = this.getKey();
                    Object other$key = other.getKey();
                    if (this$key == null) {
                        if (other$key == null) {
                            break label47;
                        }
                    } else if (this$key.equals(other$key)) {
                        break label47;
                    }

                    return false;
                }

                Object this$operation = this.getOperation();
                Object other$operation = other.getOperation();
                if (this$operation == null) {
                    if (other$operation != null) {
                        return false;
                    }
                } else if (!this$operation.equals(other$operation)) {
                    return false;
                }

                Object this$value = this.getValue();
                Object other$value = other.getValue();
                if (this$value == null) {
                    if (other$value != null) {
                        return false;
                    }
                } else if (!this$value.equals(other$value)) {
                    return false;
                }

                return true;
            }
        }
    }

    protected boolean canEqual(final Object other) {
        return other instanceof SearchCriteria;
    }

    public int hashCode() {
        int PRIME = 1;
        int result = 1;
        Object $key = this.getKey();
        result = result * 59 + ($key == null ? 43 : $key.hashCode());
        Object $operation = this.getOperation();
        result = result * 59 + ($operation == null ? 43 : $operation.hashCode());
        Object $value = this.getValue();
        result = result * 59 + ($value == null ? 43 : $value.hashCode());
        return result;
    }

    public String toString() {
        return "SearchCriteria(key=" + this.getKey() + ", operation=" + this.getOperation() + ", value=" + this.getValue() + ")";
    }
}
