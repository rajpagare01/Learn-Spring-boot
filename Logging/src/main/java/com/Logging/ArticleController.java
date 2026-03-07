package com.Logging;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@RestController
@RequestMapping("/api")
public class ArticleController {
private  static final Logger logger = LoggerFactory.getLogger(ArticleController.class);
@Autowired
private ArticleService articleService;

    @GetMapping("/hello")
    public List<Article> getArticls() {
        logger.debug("inside ArticleController.getArticles() method");
        return articleService.getArticles();
    }

}
