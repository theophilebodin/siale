package nc.mairie.siale.web;

import nc.mairie.siale.domain.TypeParam;
import org.springframework.roo.addon.web.mvc.controller.scaffold.RooWebScaffold;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/typeparams")
@Controller
@RooWebScaffold(path = "typeparams", formBackingObject = TypeParam.class)
public class TypeParamController {
}
