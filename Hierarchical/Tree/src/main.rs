/**
 * When to Use TreeNode and When Not to Use It
    Understanding TreeNode
    A TreeNode is a fundamental structure used in tree data structures, which consist of nodes connected by edges. 
    Each node contains a value and references to its child nodes. 
    Trees are widely used in various applications, such as representing hierarchical data, implementing search algorithms, and managing sorted data.

   When to Use TreeNode
    Hierarchical Data Representation: Use TreeNode when you need to represent data that has a parent-child relationship, such as organizational structures, file systems, or XML/JSON data.
    Dynamic Data Structure: Trees are suitable for scenarios where the dataset is dynamic and frequently changing. You can easily insert or delete nodes without restructuring the entire dataset.
    Searching and Sorting: Trees, especially binary search trees (BST), are effective for searching and sorting operations. They allow for efficient searching, insertion, and deletion operations (average time complexity of O(logn)).
    Balanced Trees: For applications requiring frequent insertions and deletions while maintaining sorted order, balanced trees like AVL or Red-Black trees are ideal. They ensure that operations remain efficient by keeping the tree balanced.

   When Not to Use TreeNode
    Simple Data Structures: If your data does not have a hierarchical relationship or is relatively simple (e.g., a flat list), using a TreeNode may introduce unnecessary complexity.
    Memory Overhead: Each TreeNode requires additional memory for pointers to child nodes. In cases where memory usage is critical, consider using simpler structures like arrays or linked lists.
    Performance Concerns: In scenarios where you require frequent access to elements by index (like in an array), trees may not be the best choice due to their traversal nature. Accessing elements in a tree generally has a time complexity of O(n) in the worst case.

  Advantages of Using Trees
    Efficient Searching: Trees allow for more efficient searching compared to linear data structures. Balanced trees provide logarithmic time complexity for search operations.
    Dynamic Size: Trees can grow and shrink dynamically as needed, making them flexible for applications with unpredictable sizes.
    Structured Data Representation: They provide a clear structure for representing complex relationships within data, making it easier to visualize and manage hierarchical information.

   Disadvantages of Using Trees
    Complexity in Implementation: Implementing tree structures can be more complex than using simpler data structures. Managing pointers and ensuring balance (in balanced trees) adds to the complexity.
    Potentially Unbalanced Trees: If not managed properly (e.g., with self-balancing techniques), trees can become unbalanced, leading to degraded performance with time complexities approaching O(n).
    Increased Memory Usage: Each node requires additional memory for pointers, which can be significant in large trees compared to other data structures that do not use pointers.
 */
use std::time;

#[derive(Debug)]
struct TreeNode {
    name: String,
    amount: f64,
    childrens: Vec<TreeNode>
}

impl TreeNode {
    fn new(name: &str, value: f64) -> Self {
        TreeNode {
            name: name.to_string(),
            amount: value,
            childrens: Vec::new()
        }
    }

    fn add_children(&mut self, child: TreeNode) {
        self.childrens.push(child);
    }

    fn calculate_total(&self) -> f64 {
        let mut total = self.amount;
        for child in &self.childrens {
            total += child.calculate_total();
        }
        total
    }

    fn get_item(&self, name: &str) -> Option<&TreeNode> {
        if self.name == name {
            Some(self)
        } else {
            for child in &self.childrens {
                if let Some(found) = child.get_item(name) {
                    return Some(found);
                }
            }
            None
        }
    }
}

fn main() {
    let time_now = time::Instant::now();
    let mut root = TreeNode::new("Financeiro", 0.0);

    let mut receitas = TreeNode::new("Receitas", 0.0);
    receitas.add_children(TreeNode::new("Salário", 5000.0));
    receitas.add_children(TreeNode::new("Investimentos", 2000.0));

    let mut despesas = TreeNode::new("Despesas", 0.0);
    despesas.add_children(TreeNode::new("Aluguel", -1200.0));
    despesas.add_children(TreeNode::new("Supermercado", -800.0));

    root.add_children(receitas);
    root.add_children(despesas);

    let total_financeiro = root.calculate_total();
    println!("Total Financeiro: {:.2}", total_financeiro);

    let rent = root.get_item("Aluguel");
    println!("{:#?}", root);
    println!("{:?}", rent.unwrap());
    let diff_time = time_now.elapsed().as_millis();
    println!("Tempo de execução: {}ms", diff_time);
}
