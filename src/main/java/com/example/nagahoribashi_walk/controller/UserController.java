package com.example.nagahoribashi_walk.controller;

import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.nagahoribashi_walk.dto.UserProfile;
import com.example.nagahoribashi_walk.entity.User;
import com.example.nagahoribashi_walk.form.UserProfileEditForm;
import com.example.nagahoribashi_walk.service.FavoriteService;
import com.example.nagahoribashi_walk.service.UserService;
import com.example.nagahoribashi_walk.service.userdetails.LoginUser;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
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
            @PageableDefault(size = 6) Pageable pageable,
            Model model) {

        UserProfile userProfile = userService.getProfileByUsername(loginUser.getUsername());
        UserProfileEditForm form = new UserProfileEditForm();
        BeanUtils.copyProperties(userProfile, form);

        model.addAttribute("profile", userProfile);
        model.addAttribute("userProfileEditForm", form);

        model.addAttribute("favorites",
                favoriteService.getPage(loginUser.getId(), pageable));

        model.addAttribute("activeTab", tab);
        model.addAttribute("editMode", edit);

        return "/user/mypage";
    }

    /**
     * プロフィール更新処理
     */
    @PostMapping("/mypage/profile/update")
    public String updateProfile(
            @AuthenticationPrincipal LoginUser loginUser,
            @Validated UserProfileEditForm form,
            BindingResult bindingResult,
            @PageableDefault(size = 12) Pageable pageable,
            Model model) {

        if (bindingResult.hasErrors()) {
            model.addAttribute("profile",
                    userService.getProfileByUsername(loginUser.getUsername()));

            model.addAttribute("favorites",
                    favoriteService.getPage(loginUser.getId(), pageable));
            model.addAttribute("activeTab", "profile");
            model.addAttribute("editMode", true);
            return "/user/mypage";
        }

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

    /**
     * ユーザー退会画面
     */
    @GetMapping("/unsubscribe")
    public String confirmUnsubscribe() {
        return "/user/unsubscribe";
    }

    /**
     * ユーザー退会
     */
    @PostMapping("/unsubscribe/{userId}")
    public String unsubscribe(
            @PathVariable("userId") Long userId,
            @AuthenticationPrincipal LoginUser loginUser,
            HttpServletRequest request,
            HttpServletResponse response,
            Authentication authentication,
            RedirectAttributes redirectAttributes) {

        if (!userId.equals(loginUser.getId())) {
            throw new AccessDeniedException("不正なアクセスです。");
        }

        // 退会処理
        userService.unsubscribe(loginUser.getId());
        // ログアウト処理
        new SecurityContextLogoutHandler().logout(request, response, authentication);

        return "redirect:/unsubscribe/complete";
    }

    /**
     * 退会完了画面
     */
    @GetMapping("/unsubscribe/complete")
    public String unsubscribeComplete() {
        return "/user/unsubscribe-complete";
    }
}
