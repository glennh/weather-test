import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;

public class Test_TestHelper {

	@Test
	public void GetDominantValue_1() {
		TestHelper helper = new TestHelper();
		List<Integer> l = Arrays.asList(1, 2, 2, 3, 3, 3);
		Integer dominantValue = helper.getDominantValue(l);
		assertThat(dominantValue, is(3));
	}

	@Test
	public void GetDominantValue_2() {
		TestHelper helper = new TestHelper();
		List<Integer> l = Arrays.asList(1, 2, 2, 3, 3);
		Integer dominantValue = helper.getDominantValue(l);
		assertThat(dominantValue, is(2)); // average of 2,2,3,3 rounded down
	}

	@Test
	public void GetDominantValue_3() {
		TestHelper helper = new TestHelper();
		List<Integer> l = Arrays.asList(1, 2, 2, 2, 5, 5, 5, 10, 10);
		Integer dominantValue = helper.getDominantValue(l);
		assertThat(dominantValue, is(3)); // average of 2,2,2,5,5,5 rounded
											// down;
	}

}
