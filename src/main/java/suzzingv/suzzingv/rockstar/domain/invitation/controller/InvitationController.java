package suzzingv.suzzingv.rockstar.domain.invitation.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class InvitationController {

    @GetMapping("/invite/{code}")
    public String invitePage(@PathVariable String code, Model model) {
        model.addAttribute("code", code);
        return "invite";  // templates/invite.html 렌더링
    }
}