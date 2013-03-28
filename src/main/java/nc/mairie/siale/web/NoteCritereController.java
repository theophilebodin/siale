package nc.mairie.siale.web;

import nc.mairie.siale.domain.NoteCritere;
import org.springframework.roo.addon.web.mvc.controller.scaffold.RooWebScaffold;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/notecriteres")
@Controller
@RooWebScaffold(path = "notecriteres", formBackingObject = NoteCritere.class)
public class NoteCritereController {
}
