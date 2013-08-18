package nc.mairie.siale.web;

import nc.mairie.siale.domain.Param;
import org.springframework.roo.addon.web.mvc.controller.finder.RooWebFinder;
import org.springframework.roo.addon.web.mvc.controller.scaffold.RooWebScaffold;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/params")
@Controller
@RooWebScaffold(path = "params", formBackingObject = Param.class)
@RooWebFinder
public class ParamController {
}
