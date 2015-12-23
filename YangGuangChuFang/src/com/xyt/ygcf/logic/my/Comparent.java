package com.xyt.ygcf.logic.my;

import java.text.Collator;
import java.util.Comparator;
import java.util.Locale;

import com.xyt.ygcf.entity.my.AddressBeen;

public class Comparent implements Comparator<Object> {

	@Override
	public int compare(Object arg0, Object arg1) {
		AddressBeen one = (AddressBeen) arg0;
		AddressBeen two = (AddressBeen) arg1;
		Collator ca = Collator.getInstance(Locale.CHINA);
		int flags = 0;
		if (ca.compare(one.name, two.name) < 0) {
			flags = -1;
		} else if (ca.compare(one.name, two.name) > 0) {
			flags = 1;
		} else {
			flags = 0;
		}
		return flags;
	}
}
