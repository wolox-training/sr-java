package wolox.training.utils;

import java.util.Map;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

@Component
public class PageableUtils {

  public Pageable paramsToPage(Map<String, String> params) {
    int page = Integer.parseInt(params.getOrDefault("page", "0"));
    int size = Integer.parseInt(params.getOrDefault("size", "10"));
    String sort = params.getOrDefault("sort", "id");
    removeParamsPageable(params);
    return PageRequest.of(page, size, Sort.by(sort));
  }

  private void removeParamsPageable(Map<String, String> params) {
    params.remove("page");
    params.remove("size");
    params.remove("sort");
  }
}
