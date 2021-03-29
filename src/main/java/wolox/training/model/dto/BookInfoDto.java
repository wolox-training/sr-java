package wolox.training.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BookInfoDto {

    private List<AuthorDto> authors;

    private String title;

    private String subtitle;

    private List<PublisherDto> publishers;

    @JsonProperty("publish_date")
    private String publishDate;

    @JsonProperty("number_of_pages")
    private String numberOfPages;

    private CoverDto cover;

}
