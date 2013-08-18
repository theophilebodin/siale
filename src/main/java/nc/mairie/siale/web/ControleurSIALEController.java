package nc.mairie.siale.web;

import nc.mairie.siale.domain.ControleurSIALE;
import org.springframework.roo.addon.web.mvc.controller.scaffold.RooWebScaffold;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/controleursiales")
@Controller
@RooWebScaffold(path = "controleursiales", formBackingObject = ControleurSIALE.class)
public class ControleurSIALEController {
}
