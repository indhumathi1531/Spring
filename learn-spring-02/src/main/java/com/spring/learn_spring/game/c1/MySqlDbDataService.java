package com.spring.learn_spring.game.c1;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component
@Qualifier("DbQualifier")
public class MySqlDbDataService implements DataService {
	public int[] retrieveData() {
		return new int[] { 1, 2, 3, 4, 5 };
	}

}
