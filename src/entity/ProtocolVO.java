package entity;

import java.io.Serializable;
import java.util.List;

public class ProtocolVO implements Serializable{

	private static final long serialVersionUID = -7651021741833582577L;
	private Long id;
	private Position position;
	private StateGame stateGame;
	private String title;
	private List<Moves> listMoves;
	
	
	@Override
	public String toString() {
		return "ProtocolVO [id=" + id + ", position=" + position + ", stateGame=" + stateGame + ", title=" + title
				+ ", listMoves=" + listMoves + "]";
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((listMoves == null) ? 0 : listMoves.hashCode());
		result = prime * result + ((position == null) ? 0 : position.hashCode());
		result = prime * result + ((stateGame == null) ? 0 : stateGame.hashCode());
		result = prime * result + ((title == null) ? 0 : title.hashCode());
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
		ProtocolVO other = (ProtocolVO) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (listMoves == null) {
			if (other.listMoves != null)
				return false;
		} else if (!listMoves.equals(other.listMoves))
			return false;
		if (position == null) {
			if (other.position != null)
				return false;
		} else if (!position.equals(other.position))
			return false;
		if (stateGame == null) {
			if (other.stateGame != null)
				return false;
		} else if (!stateGame.equals(other.stateGame))
			return false;
		if (title == null) {
			if (other.title != null)
				return false;
		} else if (!title.equals(other.title))
			return false;
		return true;
	}
	public Position getPosition() {
		return position;
	}
	public void setPosition(Position position) {
		this.position = position;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public List<Moves> getListMoves() {
		return listMoves;
	}
	public void setListMoves(List<Moves> listMoves) {
		this.listMoves = listMoves;
	}
	public StateGame getStateGame() {
		return stateGame;
	}
	public void setStateGame(StateGame stateGame) {
		this.stateGame = stateGame;
	}
	
}
