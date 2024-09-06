package com.example.blog.Pojo.Result;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;

import java.util.List;

@Data
@NoArgsConstructor
public class PageResult<T> {
    private Integer total;
    private List<T> list;
    public PageResult(Integer total, List<T> list) {
        this.total = total;
        this.list = list;
    }

}
