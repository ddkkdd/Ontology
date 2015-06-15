package ontology;

import java.util.List;
import model.*;

public interface FirmaService {

	List<String> getAllEmployees() throws Exception;
	
	void startReasoner();
}
