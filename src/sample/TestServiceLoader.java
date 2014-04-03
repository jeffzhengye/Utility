package sample;

import java.util.LinkedHashMap;
import java.util.ServiceLoader;

import org.apache.lucene.index.codecs.Codec;

public class TestServiceLoader {

	public static void main(String args[]) {
//		NamedSPILoader<org.apache.solr.core.CodecFactory> loader = new NamedSPILoader(
//				org.apache.solr.core.CodecFactory);
//		NamedSPILoader<Codec> loader =
//		    new NamedSPILoader<Codec>(Codec.class);
		
	    Class clazz = org.apache.lucene.index.codecs.Codec.class;
	    final ServiceLoader<Codec> loader = ServiceLoader.load(clazz);
	    final LinkedHashMap<String,Codec> services = new LinkedHashMap<String,Codec>();
	    for (final Codec service : loader) {
	      final String name = service.getName();
	      // only add the first one for each name, later services will be ignored
	      // this allows to place services before others in classpath to make 
	      // them used instead of others
	      if (!services.containsKey(name)) {
	        services.put(name, service);
	      }
	      System.out.println(name);
	    }
	}

}
