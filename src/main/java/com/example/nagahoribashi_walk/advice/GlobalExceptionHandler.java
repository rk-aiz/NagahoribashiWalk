package com.example.nagahoribashi_walk.advice;

import java.util.NoSuchElementException;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import com.example.nagahoribashi_walk.exception.CategoryAlreadyExistsException;
import com.example.nagahoribashi_walk.exception.InvalidRequestException;
import com.example.nagahoribashi_walk.exception.ResourceNotFoundException;
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

    /** 想定外のPOSTリクエストなど、不正なリクエスト値 */
    @ExceptionHandler(InvalidRequestException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String handleInvalidRequest(InvalidRequestException e, Model model) {
        log.warn("不正なリクエストを検知しました: {}", e.getMessage());
        return "error/400";
    }

    /** カテゴリ名・サブカテゴリ名の重複エラー */
    @ExceptionHandler(CategoryAlreadyExistsException.class)
    public String handleCategoryAlreadyExists(
            CategoryAlreadyExistsException e,
            RedirectAttributes redirectAttributes) {
        redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        return "redirect:/admin/category/list";
    }

    /** レビュー二重投稿エラー */
    @ExceptionHandler(ReviewAlreadyExistsException.class)
    public String handleReviewAlreadyExists(
            ReviewAlreadyExistsException e,
            RedirectAttributes redirectAttributes) {
        redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        return "redirect:/spot/" + e.getSpotId();
    }

    /** レビュー操作（更新・削除）のビジネスロジックエラー */
    @ExceptionHandler(ReviewOperationException.class)
    public String handleReviewOperation(
            ReviewOperationException e,
            RedirectAttributes redirectAttributes) {
        redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        return "redirect:/spot/" + e.getSpotId();
    }

    /** DB制約違反（ユニーク制約・FK制約など） */
    @ExceptionHandler(DataIntegrityViolationException.class)
    public String handleDataIntegrity(DataIntegrityViolationException e) {
        log.error("DB制約違反が発生しました", e);
        return "error/500";
    }

    /** リソースが見つからない場合 */
    @ExceptionHandler(ResourceNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String handleResourceNotFound(ResourceNotFoundException e, Model model) {
        model.addAttribute("message", e.getMessage());
        return "error/404";
    }

    /** リソースが見つからない場合 */
    @ExceptionHandler(NoResourceFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String handleNoResourceFound(NoResourceFoundException e, Model model) {
        model.addAttribute("message", e.getMessage());
        return "error/404";
    }

    /** 存在しないリソースへのアクセス（orElseThrow() など）*/
    @ExceptionHandler(NoSuchElementException.class)
    public String handleNoSuchElement(NoSuchElementException e) {
        log.warn("リソースが見つかりません", e);
        return "error/404";
    }

    /** 上記以外の予期しない例外 */
    @ExceptionHandler(Exception.class)
    public String handleGeneral(Exception e) {
        log.error("予期しない例外", e);
        return "error/500";
    }
}
