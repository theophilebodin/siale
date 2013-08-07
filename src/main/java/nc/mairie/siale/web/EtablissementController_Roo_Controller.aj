// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package nc.mairie.siale.web;

import java.io.UnsupportedEncodingException;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import nc.mairie.siale.domain.Etablissement;
import nc.mairie.siale.domain.Mission;
import nc.mairie.siale.web.EtablissementController;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.util.UriUtils;
import org.springframework.web.util.WebUtils;

privileged aspect EtablissementController_Roo_Controller {
    
    @RequestMapping(method = RequestMethod.POST, produces = "text/html")
    public String EtablissementController.create(@Valid Etablissement etablissement, BindingResult bindingResult, Model uiModel, HttpServletRequest httpServletRequest) {
        if (bindingResult.hasErrors()) {
            populateEditForm(uiModel, etablissement);
            return "etablissements/create";
        }
        uiModel.asMap().clear();
        etablissement.persist();
        return "redirect:/etablissements/" + encodeUrlPathSegment(etablissement.getId().toString(), httpServletRequest);
    }
    
    @RequestMapping(params = "form", produces = "text/html")
    public String EtablissementController.createForm(Model uiModel) {
        populateEditForm(uiModel, new Etablissement());
        return "etablissements/create";
    }
    
    @RequestMapping(value = "/{id}", produces = "text/html")
    public String EtablissementController.show(@PathVariable("id") Long id, Model uiModel) {
        uiModel.addAttribute("etablissement", Etablissement.findEtablissement(id));
        uiModel.addAttribute("itemId", id);
        return "etablissements/show";
    }
    
    @RequestMapping(produces = "text/html")
    public String EtablissementController.list(@RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, Model uiModel) {
        if (page != null || size != null) {
            int sizeNo = size == null ? 10 : size.intValue();
            final int firstResult = page == null ? 0 : (page.intValue() - 1) * sizeNo;
            uiModel.addAttribute("etablissements", Etablissement.findEtablissementEntries(firstResult, sizeNo));
            float nrOfPages = (float) Etablissement.countEtablissements() / sizeNo;
            uiModel.addAttribute("maxPages", (int) ((nrOfPages > (int) nrOfPages || nrOfPages == 0.0) ? nrOfPages + 1 : nrOfPages));
        } else {
            uiModel.addAttribute("etablissements", Etablissement.findAllEtablissements());
        }
        return "etablissements/list";
    }
    
    @RequestMapping(method = RequestMethod.PUT, produces = "text/html")
    public String EtablissementController.update(@Valid Etablissement etablissement, BindingResult bindingResult, Model uiModel, HttpServletRequest httpServletRequest) {
        if (bindingResult.hasErrors()) {
            populateEditForm(uiModel, etablissement);
            return "etablissements/update";
        }
        uiModel.asMap().clear();
        etablissement.merge();
        return "redirect:/etablissements/" + encodeUrlPathSegment(etablissement.getId().toString(), httpServletRequest);
    }
    
    @RequestMapping(value = "/{id}", params = "form", produces = "text/html")
    public String EtablissementController.updateForm(@PathVariable("id") Long id, Model uiModel) {
        populateEditForm(uiModel, Etablissement.findEtablissement(id));
        return "etablissements/update";
    }
    
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE, produces = "text/html")
    public String EtablissementController.delete(@PathVariable("id") Long id, @RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, Model uiModel) {
        Etablissement etablissement = Etablissement.findEtablissement(id);
        etablissement.remove();
        uiModel.asMap().clear();
        uiModel.addAttribute("page", (page == null) ? "1" : page.toString());
        uiModel.addAttribute("size", (size == null) ? "10" : size.toString());
        return "redirect:/etablissements";
    }
    
    void EtablissementController.populateEditForm(Model uiModel, Etablissement etablissement) {
        uiModel.addAttribute("etablissement", etablissement);
        uiModel.addAttribute("missions", Mission.findAllMissions());
    }
    
    String EtablissementController.encodeUrlPathSegment(String pathSegment, HttpServletRequest httpServletRequest) {
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
