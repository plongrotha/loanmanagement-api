package com.plongrotha.loanmanagement.dto.response;

import java.util.List;

import org.springframework.data.domain.Page;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class PaginationDTO<T> {

	private List<T> content;
	private int pageSize;
	private int pageNumber;
	private int totalPages;
	private Long totalElements;
	private int numberOfElements;

	private boolean last;
	private boolean first;
	private boolean empty;

	public static <T> PaginationDTO<T> fromPage(Page<T> page) {
		return PaginationDTO.<T>builder().content(page.getContent()) // Add this line
				.pageNumber(page.getNumber()).pageSize(page.getSize()).totalPages(page.getTotalPages())
				.totalElements(page.getTotalElements()).numberOfElements(page.getNumberOfElements()).last(page.isLast())
				.first(page.isFirst()).empty(page.isEmpty()).build();
	}

}
