package cz.muni.fi.pa165.ddtroops.mvc.controllers;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import cz.muni.fi.pa165.ddtroops.dto.HeroDTO;
import cz.muni.fi.pa165.ddtroops.dto.HeroUpdateDTO;
import cz.muni.fi.pa165.ddtroops.facade.HeroFacade;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.util.UriComponentsBuilder;

@Controller
@RequestMapping("/heroes")
public class HeroController {

    private final static Logger log = LoggerFactory.getLogger(HeroController.class);

    @Autowired
    private HeroFacade heroFacade;

    @RequestMapping(value="", method = RequestMethod.GET)
    public String list(Model model, HttpServletRequest request, UriComponentsBuilder uriBuilder) {
        log.debug("List all");
        model.addAttribute("heroes", heroFacade.findAll());
        return "heroes/list";
    }

    @RequestMapping(value = "/read/{id}", method = RequestMethod.GET)
    public String read(@PathVariable long id, Model model, UriComponentsBuilder uriBuilder, HttpServletRequest request) {
        log.debug(" Read ({})", id);
        model.addAttribute("hero", heroFacade.findById(id));
        return "heroes/read";
    }

    @RequestMapping(value = "/delete/{id}", method = RequestMethod.POST)
    public String delete(@PathVariable long id, Model model, HttpServletRequest request, UriComponentsBuilder uriBuilder, RedirectAttributes redirectAttributes) {

        HeroDTO hero = heroFacade.findById(id);
        heroFacade.delete(id);
        log.debug("delete hero({})", id);
        redirectAttributes.addFlashAttribute("alert_success", "Hero \"" + hero.getName() + "\" was deleted.");
        return "redirect:" + uriBuilder.path("/heroes").build().toUriString();
    }

    @RequestMapping(value = "/edit/{id}", method = RequestMethod.GET)
    public String editHero(@PathVariable long id, Model model) {

        log.debug("[HERO] Edit {}", id);
        HeroDTO heroDTO = heroFacade.findById(id);

        model.addAttribute("heroEdit", heroDTO);
        return "/heroes/edit";
    }

    @RequestMapping(value="/edit/{id}", method = RequestMethod.POST)
    public String update(@PathVariable long id,
                         @Valid @ModelAttribute("heroEdit")HeroUpdateDTO formBean,
                         BindingResult bindingResult,
                         Model model,
                         UriComponentsBuilder uriBuilder,
                         RedirectAttributes redirectAttributes,
                         HttpServletRequest request) {


        formBean.setId(id);
        log.debug("[HERO] Update: {}", formBean);
        HeroDTO result = heroFacade.update(formBean);

        String res = check(bindingResult,model, uriBuilder);
        if(res!=null) return res;

        redirectAttributes.addFlashAttribute("alert_success", "Hero " + result.getName() + " was updated");
        return "redirect:" + uriBuilder.path("/heroes/read/{id}").buildAndExpand(id).encode().toUriString();
    }

    @RequestMapping(value = "/create", method = RequestMethod.GET)
    public String createHero(Model model, HttpServletRequest request) {
        log.debug("[Hero] Create");
        model.addAttribute("heroCreate", new HeroDTO());
        return "heroes/create";
    }

    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public String create(@Valid @ModelAttribute("heroCreate") HeroDTO formBean, BindingResult bindingResult,
                           Model model, RedirectAttributes redirectAttributes, UriComponentsBuilder uriBuilder, HttpServletRequest request) {
        log.debug("Create Hero {})", formBean);

        String res = check(bindingResult,model, uriBuilder);
        if(res!=null) return res;

        HeroDTO hero = heroFacade.create(formBean);
        redirectAttributes.addFlashAttribute("alert_success", "Creation of " + hero.getName() + " succeeded");

        return "redirect:" + uriBuilder.path("/").build().toUriString();
    }

    private String check(BindingResult bindingResult, Model model, UriComponentsBuilder uriBuilder){
        if (bindingResult.hasErrors()) {
            for (ObjectError ge : bindingResult.getGlobalErrors()) {
                log.debug("ObjectError: {}", ge);
            }
            for (FieldError fe : bindingResult.getFieldErrors()) {
                model.addAttribute(fe.getField() + "_error", true);
                log.debug("FieldError: {}", fe);
            }
            return "redirect:" + uriBuilder.path("/").build().toUriString();
        }
        return null;
    }

}
