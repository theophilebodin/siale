// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package nc.mairie.siale.web;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import nc.mairie.siale.domain.Param;
import nc.mairie.siale.domain.TypeParam;
import nc.mairie.siale.web.ParamController;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.util.UriUtils;
import org.springframework.web.util.WebUtils;

privileged aspect ParamController_Roo_Controller {
    
    @RequestMapping(method = RequestMethod.POST, produces = "text/html")
    public String ParamController.create(@Valid Param param, BindingResult bindingResult, Model uiModel, HttpServletRequest httpServletRequest) {
        if (bindingResult.hasErrors()) {
            populateEditForm(uiModel, param);
            return "params/create";
        }
        uiModel.asMap().clear();
        param.persist();
        return "redirect:/params/" + encodeUrlPathSegment(param.getId().toString(), httpServletRequest);
    }
    
    @RequestMapping(params = "form", produces = "text/html")
    public String ParamController.createForm(Model uiModel) {
        populateEditForm(uiModel, new Param());
        List<String[]> dependencies = new ArrayList<String[]>();
        if (TypeParam.countTypeParams() == 0) {
            dependencies.add(new String[] { "typeparam", "typeparams" });
        }
        uiModel.addAttribute("dependencies", dependencies);
        return "params/create";
    }
    
    @RequestMapping(value = "/{id}", produces = "text/html")
    public String ParamController.show(@PathVariable("id") Long id, Model uiModel) {
        uiModel.addAttribute("param", Param.findParam(id));
        uiModel.addAttribute("itemId", id);
        return "params/show";
    }
    
    @RequestMapping(produces = "text/html")
    public String ParamController.list(@RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, Model uiModel) {
        if (page != null || size != null) {
            int sizeNo = size == null ? 10 : size.intValue();
            final int firstResult = page == null ? 0 : (page.intValue() - 1) * sizeNo;
            uiModel.addAttribute("params", Param.findParamEntries(firstResult, sizeNo));
            float nrOfPages = (float) Param.countParams() / sizeNo;
            uiModel.addAttribute("maxPages", (int) ((nrOfPages > (int) nrOfPages || nrOfPages == 0.0) ? nrOfPages + 1 : nrOfPages));
        } else {
            uiModel.addAttribute("params", Param.findAllParams());
        }
        return "params/list";
    }
    
    @RequestMapping(method = RequestMethod.PUT, produces = "text/html")
    public String ParamController.update(@Valid Param param, BindingResult bindingResult, Model uiModel, HttpServletRequest httpServletRequest) {
        if (bindingResult.hasErrors()) {
            populateEditForm(uiModel, param);
            return "params/update";
        }
        uiModel.asMap().clear();
        param.merge();
        return "redirect:/params/" + encodeUrlPathSegment(param.getId().toString(), httpServletRequest);
    }
    
    @RequestMapping(value = "/{id}", params = "form", produces = "text/html")
    public String ParamController.updateForm(@PathVariable("id") Long id, Model uiModel) {
        populateEditForm(uiModel, Param.findParam(id));
        return "params/update";
    }
    
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE, produces = "text/html")
    public String ParamController.delete(@PathVariable("id") Long id, @RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, Model uiModel) {
        Param param = Param.findParam(id);
        param.remove();
        uiModel.asMap().clear();
        uiModel.addAttribute("page", (page == null) ? "1" : page.toString());
        uiModel.addAttribute("size", (size == null) ? "10" : size.toString());
        return "redirect:/params";
    }
    
    void ParamController.populateEditForm(Model uiModel, Param param) {
        uiModel.addAttribute("param", param);
        uiModel.addAttribute("typeparams", TypeParam.findAllTypeParams());
    }
    
    String ParamController.encodeUrlPathSegment(String pathSegment, HttpServletRequest httpServletRequest) {
        String enc = httpServletRequest.getCharacterEncoding();
        if (enc == null) {
            enc = WebUtils.DEFAULT_CHARACTER_ENCODING;
        }
        try {
            pathSegment = UriUtils.encodePathSegment(pathSegment, enc);
        } catch (UnsupportedEncodingException uee) {}
        return pathSegment;
    }
    
}