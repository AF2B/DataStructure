# Stack: From Fundamentals to Advanced Concepts

## Table of Contents
1. [Introduction](#introduction)
2. [Internal Structure](#internal-structure)
3. [Basic Operations](#basic-operations)
4. [Advanced Concepts](#advanced-concepts)
5. [Best Practices](#best-practices)
6. [Performance Considerations](#performance-considerations)
7. [Real-world Examples](#real-world-examples)

## Introduction

A stack is a fundamental data structure in computer science that follows the Last-In-First-Out (LIFO) principle. It is an abstract data type that serves as a collection of elements, with two main operations: push (to add an element to the top of the stack) and pop (to remove the top element from the stack).

## Internal Structure

At its core, a stack can be implemented using various underlying data structures, such as arrays or linked lists. The choice of the underlying structure affects the performance characteristics of the stack operations.

1. Array-based implementation:
   - Uses a dynamic array to store elements.
   - The top of the stack is typically represented by the last element in the array.
   - Offers constant-time access to the top element but may require occasional resizing.

2. Linked list-based implementation:
   - Uses a singly linked list to store elements.
   - The top of the stack is represented by the head of the linked list.
   - Offers constant-time insertion and deletion at the top without the need for resizing.

## Basic Operations

### Creation and Initialization

In Ruby, we can implement a basic stack using an array:

```ruby
class Stack
  def initialize
    @elements = []
  end

  # Other methods will be added here
end
```

### Push Operation

The push operation adds an element to the top of the stack:

```ruby
class Stack
  # ...

  def push(element)
    @elements.push(element)
  end
end
```

### Pop Operation

The pop operation removes and returns the top element from the stack:

```ruby
class Stack
  # ...

  def pop
    raise 'Stack underflow' if empty?

    @elements.pop
  end
end
```

### Peek Operation

The peek operation returns the top element without removing it:

```ruby
class Stack
  # ...

  def peek
    raise 'Stack is empty' if empty?

    @elements.last
  end
end
```

### Empty Check

Checking if the stack is empty:

```ruby
class Stack
  # ...

  def empty?
    @elements.empty?
  end
end
```

### Size

Getting the size of the stack:

```ruby
class Stack
  # ...

  def size
    @elements.size
  end
end
```

## Advanced Concepts

### Generic Stack

We can create a generic stack that can hold elements of any type:

```ruby
class GenericStack
  def initialize
    @elements = []
  end

  def push(element)
    @elements.push(element)
  end

  def pop
    raise 'Stack underflow' if empty?

    @elements.pop
  end

  def peek
    raise 'Stack is empty' if empty?

    @elements.last
  end

  def empty?
    @elements.empty?
  end

  def size
    @elements.size
  end
end
```

### Stack with Maximum Element Tracking

We can extend our stack to keep track of the maximum element efficiently:

```ruby
class MaxStack
  def initialize
    @elements = []
    @max_elements = []
  end

  def push(element)
    @elements.push(element)
    if @max_elements.empty? || element >= @max_elements.last
      @max_elements.push(element)
    end
  end

  def pop
    raise 'Stack underflow' if empty?

    popped = @elements.pop
    @max_elements.pop if popped == @max_elements.last
    popped
  end

  def max
    raise 'Stack is empty' if empty?

    @max_elements.last
  end

  # Other methods (peek, empty?, size) remain the same
end
```

## Best Practices

1. Use descriptive method names (e.g., `push`, `pop`, `peek` instead of `add`, `remove`, `top`).
2. Always check for empty stack before performing pop or peek operations.
3. Consider using a custom exception for stack-specific errors.
4. Implement `to_s` or `inspect` methods for easy debugging.
5. Use composition instead of inheritance when extending stack functionality.

```ruby
class Stack
  # ...

  def to_s
    "Stack: #{@elements.reverse.join(' <- ')}"
  end
end
```

## Performance Considerations

1. Push operation: O(1) amortized time complexity (occasional resizing may occur in array-based implementations).
2. Pop operation: O(1) time complexity.
3. Peek operation: O(1) time complexity.
4. Space complexity: O(n), where n is the number of elements in the stack.

## Real-world Examples

### Example 1: Balanced Parentheses Checker

```ruby
def balanced_parentheses?(string)
  stack = Stack.new
  opening = '([{'
  closing = ')]}'
  pairs = { ')' => '(', ']' => '[', '}' => '{' }

  string.each_char do |char|
    if opening.include?(char)
      stack.push(char)
    elsif closing.include?(char)
      return false if stack.empty? || stack.pop != pairs[char]
    end
  end

  stack.empty?
end

puts balanced_parentheses?('([]){}') # true
puts balanced_parentheses?('([)]')   # false
```

### Example 2: Reverse Polish Notation Calculator

```ruby
class RPNCalculator
  def initialize
    @stack = Stack.new
  end

  def evaluate(expression)
    tokens = expression.split
    tokens.each do |token|
      if token =~ /\A-?\d+(\.\d+)?\z/
        @stack.push(token.to_f)
      else
        b, a = @stack.pop, @stack.pop
        @stack.push(calculate(a, b, token))
      end
    end
    @stack.pop
  end

  private

  def calculate(a, b, operator)
    case operator
    when '+' then a + b
    when '-' then a - b
    when '*' then a * b
    when '/' then a / b
    else raise "Unknown operator: #{operator}"
    end
  end
end

calculator = RPNCalculator.new
puts calculator.evaluate('3 4 + 2 *')   # 14
puts calculator.evaluate('5 1 2 + 4 * + 3 -')   # 14
```

### Example 3: Depth-First Search using Stack

```ruby
class Graph
  def initialize
    @adjacency_list = {}
  end

  def add_edge(u, v)
    @adjacency_list[u] ||= []
    @adjacency_list[v] ||= []
    @adjacency_list[u] << v
    @adjacency_list[v] << u
  end

  def dfs(start)
    visited = Set.new
    stack = Stack.new
    result = []

    stack.push(start)
    visited.add(start)

    until stack.empty?
      vertex = stack.pop
      result << vertex

      @adjacency_list[vertex]&.reverse_each do |neighbor|
        next if visited.include?(neighbor)

        stack.push(neighbor)
        visited.add(neighbor)
      end
    end

    result
  end
end

graph = Graph.new
graph.add_edge(0, 1)
graph.add_edge(0, 2)
graph.add_edge(1, 2)
graph.add_edge(2, 3)

puts graph.dfs(0).join(' -> ')  # 0 -> 2 -> 3 -> 1
```

These examples demonstrate the versatility and power of stacks in real-world scenarios, from syntax checking to expression evaluation and graph traversal. The use of custom stack implementations and various algorithms showcases how stacks can be leveraged to solve complex problems efficiently.