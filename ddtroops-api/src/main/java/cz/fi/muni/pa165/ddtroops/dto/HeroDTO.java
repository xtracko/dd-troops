package cz.fi.muni.pa165.ddtroops.dto;

import java.util.HashSet;
import java.util.Set;

/**
 * @author pstanko
 */
public class HeroDTO {

    private Long id;


    private String name;


    private Set<RoleDTO> roles = new HashSet<>();

    private TroopDTO troop;

    private int experience;

    HeroDTO(String name) {
        this.name = name;
    }

    HeroDTO() {}

    public HeroDTO(String name, int experience) {
        this.name = name;
        this.troop = troop;
        this.experience = experience;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<RoleDTO> getRoles() {
        return roles;
    }

    public void setRoles(Set<RoleDTO> roles) {
        this.roles = roles;
    }

    public TroopDTO getTroop() {
        return troop;
    }

    public void setTroop(TroopDTO troop) {
        this.troop = troop;
    }

    public int getExperience() {
        return experience;
    }

    public void setExperience(int experience) {
        this.experience = experience;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || !(o instanceof HeroDTO))
            return false;

        HeroDTO heroDTO = (HeroDTO) o;

        return getName() != null ? getName().equals(heroDTO.getName()) : heroDTO.getName() == null;

    }

    @Override
    public int hashCode() {
        return getName() != null ? getName().hashCode() : 0;
    }

    @Override
    public String toString() {
        return "HeroDTO{" +
            "id=" + id +
            ", name='" + name + '\'' +
            ", roles=" + roles +
            ", troop=" + troop +
            ", experience=" + experience +
            '}';
    }
}