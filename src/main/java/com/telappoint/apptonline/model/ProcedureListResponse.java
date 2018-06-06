package com.telappoint.apptonline.model;

import java.util.List;

import org.codehaus.jackson.map.annotate.JsonSerialize;
import org.codehaus.jackson.map.annotate.JsonSerialize.Inclusion;

/**
 * 
 * @author Murali G
 *
 */
@JsonSerialize(include = Inclusion.NON_NULL)
public class ProcedureListResponse extends BaseResponse {

	private String selectProcedureLabel;
	private String selectDefaultProcedureId;
	private List<Options> procedureList;

	public String getSelectProcedureLabel() {
		return selectProcedureLabel;
	}

	public void setSelectProcedureLabel(String selectProcedureLabel) {
		this.selectProcedureLabel = selectProcedureLabel;
	}

	public String getSelectDefaultProcedureId() {
		return selectDefaultProcedureId;
	}

	public void setSelectDefaultProcedureId(String selectDefaultProcedureId) {
		this.selectDefaultProcedureId = selectDefaultProcedureId;
	}

	public List<Options> getProcedureList() {
		return procedureList;
	}

	public void setProcedureList(List<Options> procedureList) {
		this.procedureList = procedureList;
	}
}

