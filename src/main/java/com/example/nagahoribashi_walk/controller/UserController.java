package com.example.nagahoribashi_walk.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;

import java.lang.ProcessBuilder.Redirect;

import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.nagahoribashi_walk.entity.User;
import com.example.nagahoribashi_walk.form.UserProfileEditForm;
import com.example.nagahoribashi_walk.service.FavoriteService;
import com.example.nagahoribashi_walk.service.UserService;
import com.example.nagahoribashi_walk.service.userdetails.LoginUser;

import lombok.RequiredArgsConstructor;

/**
 * @author 海津
 */
@Controller
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final FavoriteService favoriteService;

    @GetMapping("/mypage")
    public String mypage(
            @AuthenticationPrincipal LoginUser loginUser,
            @RequestParam(name = "tab", defaultValue = "profile") String tab,
            @RequestParam(name = "edit", defaultValue = "false") boolean edit,
            @PageableDefault(size = 12) Pageable pageable,
            Model model) {

        model.addAttribute("profile",
                userService.getProfileByUsername(loginUser.getUsername()));

        model.addAttribute("favorites",
                favoriteService.getPage(loginUser.getId(), pageable));

        model.addAttribute("activeTab", tab);
        model.addAttribute("editMode", edit);

        return "/user/mypage";
    }

    /**
     * プロフィール更新処理
     * 
     * @param loginUser ログインユーザー
     * @param form      プロフィール編集フォーム
     * @return マイページへリダイレクト
     */
    @PostMapping("/mypage/profile/update")
    public String updateProfile(
            @AuthenticationPrincipal LoginUser loginUser,
            @Valid UserProfileEditForm form) {

        // 更新用エンティティを作成
        User user = new User();

        // ログイン中ユーザー名を更新条件としてセット
        user.setUsername(loginUser.getUsername());

        // フォーム入力値をセット
        user.setEmail(form.getEmail());
        user.setDisplayName(form.getDisplayName());

        // プロフィール更新処理を実行
        userService.updateProfile(user);

        // 更新後はマイページへ戻す
        return "redirect:/mypage";
    }

    // ユーザー退会画面
    @GetMapping("/unsubscribe")
    // Authentication(誰をログアウトするか)
    // Request(セッション情報を消す)
    // Response(Cookieを消す)
    public String confirmUnsubscribe() {
        return "/user/unsubscribe";
    }

    // ユーザー退会用
    @PostMapping("/unsubscribe")
    public String unsubscribe(
            @AuthenticationPrincipal LoginUser loginUser,
            HttpServletRequest request,
            HttpServletResponse response,
            Authentication authentication,
            RedirectAttributes redirectAttributes) {

        // 退会処理
        userService.unsubscribe(loginUser.getId());

        // ログアウト処理
        new SecurityContextLogoutHandler().logout(request, response, authentication);

        return "redirect:/unsubscribe/complete";
    }

    @GetMapping("/unsubscribe/complete")
    public String unsubscribeComplete() {
        return "/user/unsubscribe-complete";
    }
}
