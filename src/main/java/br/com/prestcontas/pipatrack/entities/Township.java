package br.com.prestcontas.pipatrack.entities;

import java.util.List;
import java.util.UUID;

import org.hibernate.validator.constraints.br.CNPJ;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;

@Entity
@Table(name = "tb_township")
public class Township {
    
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "township_id")
    private UUID townshipId;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, length = 2)
    private String uf;

    @CNPJ
    @NotBlank
    private String cnpj;

    @Column(nullable = false, unique = true)
    private Long code;

    @Column(name = "image_url", nullable = false)
    private String imageUrl;

    @OneToMany(
        mappedBy = "township",
        cascade = CascadeType.REMOVE,
        orphanRemoval = true
    )
    private List<User> users;

    public UUID getTownshipId() {
        return townshipId;
    }

    public void setTownshipId(UUID townshipId) {
        this.townshipId = townshipId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUf() {
        return uf;
    }

    public void setUf(String uf) {
        this.uf = uf;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getCnpj() {
        return cnpj;
    }

    public void setCnpj(String cnpj) {
        this.cnpj = cnpj;
    }

    public Long getCode() {
        return code;
    }

    public void setCode(Long code) {
        this.code = code;
    }

    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }
}
