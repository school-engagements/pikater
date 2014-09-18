package org.pikater.web.unused.experiment.resources;

import org.pikater.shared.util.SimpleIDGenerator;

/**
 * Class encapsulating a resource and providing identification and simple description to it.
 * 
 * IMPORTANT NOTE:
 * This class's equals method is relied upon in child classes and it MUST ONLY CHECK A SINGLE NON-STATIC FIELD
 * which is also IMMUTABLE - in this case the @id field. The @description field will be subject
 * to translation if the application supports multiple locales and so this field should not be involved at all.
 * 
 * See the child classes for more details.
 */
public class Resource
{
	private static SimpleIDGenerator resourceCounter = new SimpleIDGenerator();
	
	public final Integer id;
	public final String description;

	public Resource(String description)
	{
		this.id = resourceCounter.getAndIncrement();
		this.description = description;
	}
	
	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj)
	{
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Resource other = (Resource) obj;
		if (id == null)
		{
			if (other.id != null)
				return false;
		}
		else if (!id.equals(other.id))
			return false;
		return true;
	}
}