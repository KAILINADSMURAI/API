package base;

import org.openqa.selenium.WebDriverException;

public interface CatchAllExceptionSolver {
	public void solveException(WebDriverException ex) throws WebDriverException;
}
