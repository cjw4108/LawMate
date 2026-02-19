package com.lawmate.dto;

public class LegalDicDTO {

    private Long id;
    private String term;
    private String law;
    private String description;

    public LegalDicDTO() {}

    public LegalDicDTO(Long id, String term, String law, String description) {
        this.id          = id;
        this.term        = term;
        this.law         = law;
        this.description = description;
    }

    public Long getId()               { return id; }
    public void setId(Long id)        { this.id = id; }

    public String getTerm()                  { return term; }
    public void   setTerm(String term)       { this.term = term; }

    public String getLaw()                   { return law; }
    public void   setLaw(String law)         { this.law = law; }

    public String getDescription()                       { return description; }
    public void   setDescription(String description)     { this.description = description; }
}