package ti.zai.bifilm.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ti.zai.bifilm.models.Actor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ActorDTO {
	private String name;
	private String surname;

	public ActorDTO(Actor actor) {
		this.name = actor.getName();
		this.surname = actor.getSurname();
	}
}
