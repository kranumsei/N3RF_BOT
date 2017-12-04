package role.season;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "MEMBROS")

public class Membro {
	public Membro() {
		
	}
	
	public Membro(String id2, String nome, Long pontosTemp, Long pontosTotais, Integer modifier) {
		super();
		this.id = id2;
		this.nome = nome;
		this.pontosTemp = pontosTemp;
		this.pontosTotais = pontosTotais;
		this.modifier = modifier;
	}
	@Id
	@Column
	private String id;
	@Column
	private String nome;
	@Column
	private Long pontosTemp;
	@Column
	private Long pontosTotais;
	@Column
	private Integer modifier;
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Membro other = (Membro) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getNome() {
		return nome;
	}
	public void setNome(String nome) {
		this.nome = nome;
	}
	public Long getPontosTemp() {
		return pontosTemp;
	}
	public void setPontosTemp(Long pontosTemp) {
		this.pontosTemp = pontosTemp;
	}
	public Long getPontosTotais() {
		return pontosTotais;
	}
	public void setPontosTotais(Long pontosTotais) {
		this.pontosTotais = pontosTotais;
	}
	public Integer getModifier() {
		return modifier;
	}
	public void setModificador(Integer modificador) {
		this.modifier = modificador;
	}
}
