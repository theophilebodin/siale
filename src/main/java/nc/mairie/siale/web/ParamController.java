package nc.mairie.siale.web;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import nc.mairie.siale.domain.Param;
import nc.mairie.siale.domain.TypeParam;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.util.UriUtils;
import org.springframework.web.util.WebUtils;

@RequestMapping("/params")
@Controller
public class ParamController {

	@RequestMapping(method = RequestMethod.POST, produces = "text/html")
    public String create(@Valid Param param, BindingResult bindingResult, Model uiModel, HttpServletRequest httpServletRequest) {
        if (bindingResult.hasErrors()) {
            populateEditForm(uiModel, param);
            return "params/create";
        }
        uiModel.asMap().clear();
        param.persist();
        return "redirect:/params/" + encodeUrlPathSegment(param.getId().toString(), httpServletRequest);
    }

	@RequestMapping(params = "form", produces = "text/html")
    public String createForm(Model uiModel) {
        populateEditForm(uiModel, new Param());
        List<String[]> dependencies = new ArrayList<String[]>();
        if (TypeParam.countTypeParams() == 0) {
            dependencies.add(new String[] { "typeParam", "typeparams" });
        }
        uiModel.addAttribute("dependencies", dependencies);
        return "params/create";
    }

	@RequestMapping(value = "/{id}", produces = "text/html")
    public String show(@PathVariable("id") Long id, Model uiModel) {
        uiModel.addAttribute("param", Param.findParam(id));
        uiModel.addAttribute("itemId", id);
        return "params/show";
    }

	@RequestMapping(produces = "text/html")
    public String list(@RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, @RequestParam(value = "sortFieldName", required = false) String sortFieldName, @RequestParam(value = "sortOrder", required = false) String sortOrder, Model uiModel) {
        if (page != null || size != null) {
            int sizeNo = size == null ? 10 : size.intValue();
            final int firstResult = page == null ? 0 : (page.intValue() - 1) * sizeNo;
            uiModel.addAttribute("params", Param.findParamEntries(firstResult, sizeNo, sortFieldName, sortOrder));
            float nrOfPages = (float) Param.countParams() / sizeNo;
            uiModel.addAttribute("maxPages", (int) ((nrOfPages > (int) nrOfPages || nrOfPages == 0.0) ? nrOfPages + 1 : nrOfPages));
        } else {
            uiModel.addAttribute("params", Param.findAllParams(sortFieldName, sortOrder));
        }
        return "params/list";
    }

	@RequestMapping(method = RequestMethod.PUT, produces = "text/html")
    public String update(@Valid Param param, BindingResult bindingResult, Model uiModel, HttpServletRequest httpServletRequest) {
        if (bindingResult.hasErrors()) {
            populateEditForm(uiModel, param);
            return "params/update";
        }
        uiModel.asMap().clear();
        param.merge();
        return "redirect:/params/" + encodeUrlPathSegment(param.getId().toString(), httpServletRequest);
    }

	@RequestMapping(value = "/{id}", params = "form", produces = "text/html")
    public String updateForm(@PathVariable("id") Long id, Model uiModel) {
        populateEditForm(uiModel, Param.findParam(id));
        return "params/update";
    }

	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE, produces = "text/html")
    public String delete(@PathVariable("id") Long id, @RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, Model uiModel) {
        Param param = Param.findParam(id);
        param.remove();
        uiModel.asMap().clear();
        uiModel.addAttribute("page", (page == null) ? "1" : page.toString());
        uiModel.addAttribute("size", (size == null) ? "10" : size.toString());
        return "redirect:/params";
    }

	void populateEditForm(Model uiModel, Param param) {
        uiModel.addAttribute("param", param);
        uiModel.addAttribute("typeparams", TypeParam.findAllTypeParams());
    }

	String encodeUrlPathSegment(String pathSegment, HttpServletRequest httpServletRequest) {
        String enc = httpServletRequest.getCharacterEncoding();
        if (enc == null) {
            enc = WebUtils.DEFAULT_CHARACTER_ENCODING;
        }
        try {
            pathSegment = UriUtils.encodePathSegment(pathSegment, enc);
        } catch (UnsupportedEncodingException uee) {}
        return pathSegment;
    }

	@RequestMapping(params = { "find=ByNomLike", "form" }, method = RequestMethod.GET)
    public String findParamsByNomLikeForm(Model uiModel) {
        return "params/findParamsByNomLike";
    }

	@RequestMapping(params = "find=ByNomLike", method = RequestMethod.GET)
    public String findParamsByNomLike(@RequestParam("nom") String nom, @RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, @RequestParam(value = "sortFieldName", required = false) String sortFieldName, @RequestParam(value = "sortOrder", required = false) String sortOrder, Model uiModel) {
        if (page != null || size != null) {
            int sizeNo = size == null ? 10 : size.intValue();
            final int firstResult = page == null ? 0 : (page.intValue() - 1) * sizeNo;
            uiModel.addAttribute("params", Param.findParamsByNomLike(nom, sortFieldName, sortOrder).setFirstResult(firstResult).setMaxResults(sizeNo).getResultList());
            float nrOfPages = (float) Param.countFindParamsByNomLike(nom) / sizeNo;
            uiModel.addAttribute("maxPages", (int) ((nrOfPages > (int) nrOfPages || nrOfPages == 0.0) ? nrOfPages + 1 : nrOfPages));
        } else {
            uiModel.addAttribute("params", Param.findParamsByNomLike(nom, sortFieldName, sortOrder).getResultList());
        }
        return "params/list";
    }

	@RequestMapping(params = { "find=ByTypeParam", "form" }, method = RequestMethod.GET)
    public String findParamsByTypeParamForm(Model uiModel) {
        uiModel.addAttribute("typeparams", TypeParam.findAllTypeParams());
        return "params/findParamsByTypeParam";
    }

	@RequestMapping(params = "find=ByTypeParam", method = RequestMethod.GET)
    public String findParamsByTypeParam(@RequestParam("typeParam") TypeParam typeParam, @RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, @RequestParam(value = "sortFieldName", required = false) String sortFieldName, @RequestParam(value = "sortOrder", required = false) String sortOrder, Model uiModel) {
        if (page != null || size != null) {
            int sizeNo = size == null ? 10 : size.intValue();
            final int firstResult = page == null ? 0 : (page.intValue() - 1) * sizeNo;
            uiModel.addAttribute("params", Param.findParamsByTypeParam(typeParam, sortFieldName, sortOrder).setFirstResult(firstResult).setMaxResults(sizeNo).getResultList());
            float nrOfPages = (float) Param.countFindParamsByTypeParam(typeParam) / sizeNo;
            uiModel.addAttribute("maxPages", (int) ((nrOfPages > (int) nrOfPages || nrOfPages == 0.0) ? nrOfPages + 1 : nrOfPages));
        } else {
            uiModel.addAttribute("params", Param.findParamsByTypeParam(typeParam, sortFieldName, sortOrder).getResultList());
        }
        return "params/list";
    }
}
