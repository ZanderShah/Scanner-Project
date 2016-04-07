public class BinaryTreeNode<E extends Comparable<E>>
{
	private E item;
	private BinaryTreeNode<E> left, right;

	public BinaryTreeNode(E item)
	{
		this.item = item;
		left = right = null;
	}
	
	public BinaryTreeNode<E> getLeft()
	{
		return left;
	}
	
	public BinaryTreeNode<E> getRight()
	{
		return right;
	}
	
	public void setLeft(BinaryTreeNode<E> left)
	{
		this.left = left;
	}
	
	public void setRight(BinaryTreeNode<E> right)
	{
		this.right = right;
	}
	
	public E getItem()
	{
		return item;
	}
	
	public void setItem(E item)
	{
		this.item = item;
	}
	
	public boolean isLeaf()
	{
		return left == null && right == null;
	}
	
	public String toString()
	{
		return item + "";
	}
}
