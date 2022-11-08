package it.ghismo.corso1.priceart.entities;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonBackReference;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.ToString;


@Entity
@Table(name="dettlistini")
@Data
@NoArgsConstructor
@RequiredArgsConstructor
@AllArgsConstructor
//@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class DettaglioListino implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO /*, generator = "hibernate_sequence"*/) // hibernate_sequence è la sequence di default che viene usata da hibernate... deve essere presente la sequence con questo nome sul DB
	@Column(name="id")
	private Integer id;

	@NonNull
	@Column(name="codart", nullable = false)
	@NotNull(message = "NotNull.DettListini.codArt.Validation")
	@Size(min = 5, max = 20, message = "Size.DettListini.codArt.Validation")
	private String codArt;

	/*
	@Column(name="idlist", nullable = false)
	private String idList;
	*/
	
	@NonNull
	@Column(name="prezzo", nullable = false)
	@Min(value = (long)0.01, message = "Min.DettListini.prezzo.Validation")
	private Double prezzo;

	@NonNull
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name = "idlist", referencedColumnName = "id")
	@NotNull(message = "NotNull.DettListini.idList.Validation")
	@ToString.Exclude
	@JsonBackReference
	private Listino listino;

	
	@ToString.Include(name = "id listino")
	private String listinoToString() {
		return listino != null ? listino.getId() : null;
	}
}