// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package nc.mairie.siale.web;

import java.io.UnsupportedEncodingException;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import nc.mairie.siale.domain.TypeParam;
import nc.mairie.siale.web.TypeParamController;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.util.UriUtils;
import org.springframework.web.util.WebUtils;

privileged aspect TypeParamController_Roo_Controller {
    
    @RequestMapping(method = RequestMethod.POST, produces = "text/html")
    public String TypeParamController.create(@Valid TypeParam typeParam, BindingResult bindingResult, Model uiModel, HttpServletRequest httpServletRequest) {
        if (bindingResult.hasErrors()) {
            populateEditForm(uiModel, typeParam);
            return "typeparams/create";
        }
        uiModel.asMap().clear();
        typeParam.persist();
        return "redirect:/typeparams/" + encodeUrlPathSegment(typeParam.getId().toString(), httpServletRequest);
    }
    
    @RequestMapping(params = "form", produces = "text/html")
    public String TypeParamController.createForm(Model uiModel) {
        populateEditForm(uiModel, new TypeParam());
        return "typeparams/create";
    }
    
    @RequestMapping(value = "/{id}", produces = "text/html")
    public String TypeParamController.show(@PathVariable("id") Long id, Model uiModel) {
        uiModel.addAttribute("typeparam", TypeParam.findTypeParam(id));
        uiModel.addAttribute("itemId", id);
        return "typeparams/show";
    }
    
    @RequestMapping(produces = "text/html")
    public String TypeParamController.list(@RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, @RequestParam(value = "sortFieldName", required = false) String sortFieldName, @RequestParam(value = "sortOrder", required = false) String sortOrder, Model uiModel) {
        if (page != null || size != null) {
            int sizeNo = size == null ? 10 : size.intValue();
            final int firstResult = page == null ? 0 : (page.intValue() - 1) * sizeNo;
            uiModel.addAttribute("typeparams", TypeParam.findTypeParamEntries(firstResult, sizeNo, sortFieldName, sortOrder));
            float nrOfPages = (float) TypeParam.countTypeParams() / sizeNo;
            uiModel.addAttribute("maxPages", (int) ((nrOfPages > (int) nrOfPages || nrOfPages == 0.0) ? nrOfPages + 1 : nrOfPages));
        } else {
            uiModel.addAttribute("typeparams", TypeParam.findAllTypeParams(sortFieldName, sortOrder));
        }
        return "typeparams/list";
    }
    
    @RequestMapping(method = RequestMethod.PUT, produces = "text/html")
    public String TypeParamController.update(@Valid TypeParam typeParam, BindingResult bindingResult, Model uiModel, HttpServletRequest httpServletRequest) {
        if (bindingResult.hasErrors()) {
            populateEditForm(uiModel, typeParam);
            return "typeparams/update";
        }
        uiModel.asMap().clear();
        typeParam.merge();
        return "redirect:/typeparams/" + encodeUrlPathSegment(typeParam.getId().toString(), httpServletRequest);
    }
    
    @RequestMapping(value = "/{id}", params = "form", produces = "text/html")
    public String TypeParamController.updateForm(@PathVariable("id") Long id, Model uiModel) {
        populateEditForm(uiModel, TypeParam.findTypeParam(id));
        return "typeparams/update";
    }
    
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE, produces = "text/html")
    public String TypeParamController.delete(@PathVariable("id") Long id, @RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, Model uiModel) {
        TypeParam typeParam = TypeParam.findTypeParam(id);
        typeParam.remove();
        uiModel.asMap().clear();
        uiModel.addAttribute("page", (page == null) ? "1" : page.toString());
        uiModel.addAttribute("size", (size == null) ? "10" : size.toString());
        return "redirect:/typeparams";
    }
    
    void TypeParamController.populateEditForm(Model uiModel, TypeParam typeParam) {
        uiModel.addAttribute("typeParam", typeParam);
    }
    
    String TypeParamController.encodeUrlPathSegment(String pathSegment, HttpServletRequest httpServletRequest) {
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
