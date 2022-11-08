package it.ghismo.corso1.priceart.entities;

import java.io.Serializable;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;


@Entity
@Table(name="listini")
@Data
@NoArgsConstructor
@RequiredArgsConstructor
//@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class Listino implements Serializable {
	private static final long serialVersionUID = 1L;
	
	@NonNull
	@Id
	@Column(name="id", nullable = false)
	private String id;
	
	@NonNull
	@Column(name="descrizione", nullable = false)
	@NotNull(message = "Size.Listini.descrizione.Validation")
	private String descrizione;
	
	@NonNull
	@Column(name="obsoleto", nullable = false)
	private String obsoleto;

	@EqualsAndHashCode.Exclude
	@OneToMany(mappedBy="listino", fetch = FetchType.LAZY, cascade={CascadeType.ALL})
	@JsonManagedReference
	private Set<DettaglioListino> dettagli;



}