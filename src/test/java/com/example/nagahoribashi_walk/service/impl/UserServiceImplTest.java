package com.example.nagahoribashi_walk.service.impl;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mybatis.spring.boot.test.autoconfigure.MybatisTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.TestPropertySource;

import com.example.nagahoribashi_walk.entity.User;

/**
 * UserServiceImpl 結合テスト
 *
 * <p>
 * {@link MybatisTest} はMyBatis関連のBeanのみをロードし、H2インメモリDBを自動構成する。
 * テスト後は {@code @Transactional} によって自動ロールバックされるため、テスト間の独立性が保たれる。
 */
@MybatisTest
@Import(UserServiceImpl.class)
@TestPropertySource(properties = {
        "spring.sql.init.schema-locations=classpath:schema-h2.sql",
        "spring.sql.init.data-locations="
})
class UserServiceImplTest {

    @Autowired
    private UserServiceImpl userService;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @BeforeEach
    void setUp() {
        jdbcTemplate.update(
                "INSERT INTO users (username, password, email, role, display_name) VALUES (?, ?, ?, ?, ?)",
                "user1", "pass1", "user1@test.com", "USER", "ユーザー1");
        jdbcTemplate.update(
                "INSERT INTO users (username, password, email, role, display_name) VALUES (?, ?, ?, ?, ?)",
                "user2", "pass2", "user2@test.com", "USER", "ユーザー2");
        jdbcTemplate.update(
                "INSERT INTO users (username, password, email, role, display_name) VALUES (?, ?, ?, ?, ?)",
                "user3", "pass3", "user3@test.com", "USER", "ユーザー3");
    }

    @Test
    void getPage_1ページ目_件数とページ情報が正しい() {
        Page<User> page = userService.getPage(PageRequest.of(0, 2));

        assertThat(page.getTotalElements()).isEqualTo(3);
        assertThat(page.getContent()).hasSize(2);
        assertThat(page.getTotalPages()).isEqualTo(2);
        assertThat(page.getContent().get(0).getUsername()).isEqualTo("user1");
    }

    @Test
    void getPage_最終ページ_残りの件数が正しい() {
        Page<User> page = userService.getPage(PageRequest.of(1, 2));

        assertThat(page.getContent()).hasSize(1);
        assertThat(page.getContent().get(0).getUsername()).isEqualTo("user3");
    }
}
