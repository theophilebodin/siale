package nc.mairie.siale.web;

import nc.mairie.siale.domain.Notation;
import org.springframework.roo.addon.web.mvc.controller.scaffold.RooWebScaffold;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/notations")
@Controller
@RooWebScaffold(path = "notations", formBackingObject = Notation.class)
public class NotationController {
}
