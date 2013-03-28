// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package nc.mairie.siale.web;

import java.io.UnsupportedEncodingException;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import nc.mairie.siale.domain.ControleurSIALE;
import nc.mairie.siale.domain.Droit;
import nc.mairie.siale.web.ControleurSIALEController;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.util.UriUtils;
import org.springframework.web.util.WebUtils;

privileged aspect ControleurSIALEController_Roo_Controller {
    
    @RequestMapping(method = RequestMethod.POST, produces = "text/html")
    public String ControleurSIALEController.create(@Valid ControleurSIALE controleurSIALE, BindingResult bindingResult, Model uiModel, HttpServletRequest httpServletRequest) {
        if (bindingResult.hasErrors()) {
            populateEditForm(uiModel, controleurSIALE);
            return "controleursiales/create";
        }
        uiModel.asMap().clear();
        controleurSIALE.persist();
        return "redirect:/controleursiales/" + encodeUrlPathSegment(controleurSIALE.getId().toString(), httpServletRequest);
    }
    
    @RequestMapping(params = "form", produces = "text/html")
    public String ControleurSIALEController.createForm(Model uiModel) {
        populateEditForm(uiModel, new ControleurSIALE());
        return "controleursiales/create";
    }
    
    @RequestMapping(value = "/{id}", produces = "text/html")
    public String ControleurSIALEController.show(@PathVariable("id") Long id, Model uiModel) {
        uiModel.addAttribute("controleursiale", ControleurSIALE.findControleurSIALE(id));
        uiModel.addAttribute("itemId", id);
        return "controleursiales/show";
    }
    
    @RequestMapping(produces = "text/html")
    public String ControleurSIALEController.list(@RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, Model uiModel) {
        if (page != null || size != null) {
            int sizeNo = size == null ? 10 : size.intValue();
            final int firstResult = page == null ? 0 : (page.intValue() - 1) * sizeNo;
            uiModel.addAttribute("controleursiales", ControleurSIALE.findControleurSIALEEntries(firstResult, sizeNo));
            float nrOfPages = (float) ControleurSIALE.countControleurSIALEs() / sizeNo;
            uiModel.addAttribute("maxPages", (int) ((nrOfPages > (int) nrOfPages || nrOfPages == 0.0) ? nrOfPages + 1 : nrOfPages));
        } else {
            uiModel.addAttribute("controleursiales", ControleurSIALE.findAllControleurSIALEs());
        }
        return "controleursiales/list";
    }
    
    @RequestMapping(method = RequestMethod.PUT, produces = "text/html")
    public String ControleurSIALEController.update(@Valid ControleurSIALE controleurSIALE, BindingResult bindingResult, Model uiModel, HttpServletRequest httpServletRequest) {
        if (bindingResult.hasErrors()) {
            populateEditForm(uiModel, controleurSIALE);
            return "controleursiales/update";
        }
        uiModel.asMap().clear();
        controleurSIALE.merge();
        return "redirect:/controleursiales/" + encodeUrlPathSegment(controleurSIALE.getId().toString(), httpServletRequest);
    }
    
    @RequestMapping(value = "/{id}", params = "form", produces = "text/html")
    public String ControleurSIALEController.updateForm(@PathVariable("id") Long id, Model uiModel) {
        populateEditForm(uiModel, ControleurSIALE.findControleurSIALE(id));
        return "controleursiales/update";
    }
    
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE, produces = "text/html")
    public String ControleurSIALEController.delete(@PathVariable("id") Long id, @RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, Model uiModel) {
        ControleurSIALE controleurSIALE = ControleurSIALE.findControleurSIALE(id);
        controleurSIALE.remove();
        uiModel.asMap().clear();
        uiModel.addAttribute("page", (page == null) ? "1" : page.toString());
        uiModel.addAttribute("size", (size == null) ? "10" : size.toString());
        return "redirect:/controleursiales";
    }
    
    void ControleurSIALEController.populateEditForm(Model uiModel, ControleurSIALE controleurSIALE) {
        uiModel.addAttribute("controleurSIALE", controleurSIALE);
        uiModel.addAttribute("droits", Droit.findAllDroits());
    }
    
    String ControleurSIALEController.encodeUrlPathSegment(String pathSegment, HttpServletRequest httpServletRequest) {
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
