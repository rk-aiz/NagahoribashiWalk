package com.example.nagahoribashi_walk.advice;

import java.util.NoSuchElementException;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.nagahoribashi_walk.exception.CategoryAlreadyExistsException;
import com.example.nagahoribashi_walk.exception.ReviewAlreadyExistsException;
import com.example.nagahoribashi_walk.exception.ReviewOperationException;

import lombok.extern.slf4j.Slf4j;

/**
 * アプリケーション全体の例外ハンドラー。
 * Controller で個別に try-catch せず、ここに集約する
 *
 * @author 海津
 */
@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {

    /**
     * カテゴリ名・サブカテゴリ名の重複エラー
     * カテゴリ管理画面へリダイレクト
     */
    @ExceptionHandler(CategoryAlreadyExistsException.class)
    public String handleCategoryAlreadyExists(
            CategoryAlreadyExistsException e,
            RedirectAttributes redirectAttributes) {
        redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        return "redirect:/admin/category/list";
    }

    /**
     * レビュー二重投稿エラー
     * 例外が持つ spotId を使ってスポット詳細画面へリダイレクト
     */
    @ExceptionHandler(ReviewAlreadyExistsException.class)
    public String handleReviewAlreadyExists(
            ReviewAlreadyExistsException e,
            RedirectAttributes redirectAttributes) {
        redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        return "redirect:/spot/" + e.getSpotId();
    }

    /**
     * レビュー操作（更新・削除）のビジネスロジックエラー
     * 例外が持つ spotId を使ってスポット詳細画面へリダイレクト
     */
    @ExceptionHandler(ReviewOperationException.class)
    public String handleReviewOperation(
            ReviewOperationException e,
            RedirectAttributes redirectAttributes) {
        redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        return "redirect:/spot/" + e.getSpotId();
    }

    /**
     * DB制約違反（ユニーク制約・FK制約など）
     * アプリ層で防ぎきれなかった場合に到達する
     */
    @ExceptionHandler(DataIntegrityViolationException.class)
    public String handleDataIntegrity(DataIntegrityViolationException e) {
        log.error("DB制約違反が発生しました", e);
        return "error/500";
    }

    /**
     * 存在しないリソースへのアクセス（orElseThrow() など）
     */
    @ExceptionHandler(NoSuchElementException.class)
    public String handleNoSuchElement(NoSuchElementException e) {
        log.warn("リソースが見つかりません", e);
        return "error/404";
    }

    /**
     * 上記以外の予期しない例外。
     */
    @ExceptionHandler(Exception.class)
    public String handleGeneral(Exception e) {
        log.error("予期しない例外", e);
        return "error/500";
    }
}
