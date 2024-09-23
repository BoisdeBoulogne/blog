package com.example.blog.Pojo.dto;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class SearchArticlesByTagIdDTO {

    private Long tagId;

    private Integer pageNum;
}
