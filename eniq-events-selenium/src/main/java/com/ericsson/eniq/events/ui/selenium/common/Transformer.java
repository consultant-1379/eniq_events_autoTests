package com.ericsson.eniq.events.ui.selenium.common;

import java.sql.ResultSet;
import java.sql.SQLException;

public interface Transformer<T> {
	/**
	 * Transform result set into expected output
	 */
	T transform(ResultSet resultSet) throws SQLException;

}
