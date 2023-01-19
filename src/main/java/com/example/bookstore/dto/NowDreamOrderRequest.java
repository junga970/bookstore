package com.example.bookstore.dto;

import java.util.List;
import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class NowDreamOrderRequest {

	@NotEmpty
	List<@Valid Long> cartItemIds;
}
