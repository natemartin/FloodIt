import java.util.Comparator;

public class NodeComparator implements Comparator<Node>
{
	

	@Override
	public int compare(Node o1, Node o2) {
		if(o1.fCost == o2.fCost)
		{
			if(o1.hCost > o2.hCost)
			{
				return 1;
			}
			else
			{
				return -1;
			}
		}
		if(o1.fCost > o2.fCost)
		{
			return 1;
		}
		else
		{
			return -1;
		}
	}
}