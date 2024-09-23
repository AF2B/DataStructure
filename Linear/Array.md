# Arrays: From Fundamentals to Advanced Concepts

## Table of Contents
1. [Introduction](#introduction)
2. [Internal Structure](#internal-structure)
3. [Basic Operations](#basic-operations)
4. [Advanced Concepts](#advanced-concepts)
5. [Best Practices](#best-practices)
6. [Performance Considerations](#performance-considerations)
7. [Real-world Examples](#real-world-examples)

## Introduction

An array is a fundamental data structure in computer science, consisting of a collection of elements, each identified by an index or a key. Arrays are used to store multiple values in a single variable, providing efficient access to elements based on their position.

## Internal Structure

At the lowest level, an array is a contiguous block of memory, with each element occupying a fixed amount of space. This contiguous nature allows for constant-time access to elements given their index, as the memory address can be calculated using a simple formula:

```
element_address = base_address + (index * element_size)
```

In many high-level languages, arrays are implemented as dynamic data structures that can grow or shrink as needed. These dynamic arrays (like Ruby's Array class) typically use an underlying static array and resize it when necessary.

## Basic Operations

### Creation and Initialization

In Ruby, arrays can be created and initialized in various ways:

```ruby
# Empty array
empty_array = []

# Array with initial values
numbers = [1, 2, 3, 4, 5]

# Array of strings
fruits = %w[apple banana cherry]

# Array with a specific size and default value
fixed_size_array = Array.new(5, 0) # [0, 0, 0, 0, 0]
```

### Accessing Elements

Elements in an array are typically accessed using zero-based indexing:

```ruby
fruits = %w[apple banana cherry]
first_fruit = fruits[0] # "apple"
last_fruit = fruits[-1] # "cherry"

# Slicing
subset = fruits[1..2] # ["banana", "cherry"]
```

### Modifying Arrays

Arrays can be modified using various methods:

```ruby
numbers = [1, 2, 3, 4, 5]

# Adding elements
numbers << 6 # Append
numbers.push(7) # Append
numbers.unshift(0) # Prepend

# Removing elements
numbers.pop # Remove and return last element
numbers.shift # Remove and return first element
numbers.delete(3) # Remove all occurrences of 3

# Updating elements
numbers[0] = 10 # Replace first element
```

## Advanced Concepts

### Multi-dimensional Arrays

Arrays can contain other arrays, creating multi-dimensional structures:

```ruby
matrix = [
  [1, 2, 3],
  [4, 5, 6],
  [7, 8, 9]
]

element = matrix[1][2] # 6
```

### Array Methods and Transformations

Ruby provides powerful methods for working with arrays:

```ruby
numbers = [1, 2, 3, 4, 5]

# Mapping
squared = numbers.map { |n| n**2 } # [1, 4, 9, 16, 25]

# Filtering
even_numbers = numbers.select(&:even?) # [2, 4]

# Reducing
sum = numbers.reduce(:+) # 15

# Sorting
sorted_desc = numbers.sort.reverse # [5, 4, 3, 2, 1]
```

### Custom Sorting

Arrays can be sorted using custom criteria:

```ruby
fruits = %w[apple banana cherry date elderberry]
sorted_by_length = fruits.sort_by(&:length)
# ["date", "apple", "banana", "cherry", "elderberry"]
```

## Best Practices

1. Use descriptive variable names for arrays (e.g., `user_names` instead of `arr`).
2. Prefer array methods over manual iteration when possible.
3. Use `freeze` for immutable arrays to prevent accidental modifications.
4. Use `Array#fetch` with a default value or block to handle out-of-bounds access safely.

```ruby
DAYS_OF_WEEK = %w[Sunday Monday Tuesday Wednesday Thursday Friday Saturday].freeze

def day_name(index)
  DAYS_OF_WEEK.fetch(index) { |i| raise ArgumentError, "Invalid day index: #{i}" }
end
```

## Performance Considerations

1. Accessing elements by index is O(1) (constant time).
2. Inserting or deleting elements at the beginning of an array is O(n) as it requires shifting all subsequent elements.
3. Appending elements to the end of an array is generally O(1), but may occasionally be O(n) when resizing is required.
4. Searching for an element in an unsorted array is O(n).
5. Sorting an array is typically O(n log n) for comparison-based sorts.

## Real-world Examples

### Example 1: Processing CSV Data

```ruby
require 'csv'

def process_sales_data(file_path)
  sales_data = CSV.read(file_path, headers: true)
  total_sales = sales_data['amount'].map(&:to_f).sum
  top_products = sales_data.group_by { |row| row['product'] }
                           .transform_values { |rows| rows.sum { |r| r['amount'].to_f } }
                           .sort_by { |_, amount| -amount }
                           .first(5)

  {
    total_sales: total_sales,
    top_products: top_products
  }
end

result = process_sales_data('sales.csv')
puts "Total Sales: $#{result[:total_sales]}"
puts "Top 5 Products:"
result[:top_products].each do |product, amount|
  puts "  #{product}: $#{amount}"
end
```

### Example 2: Image Processing

```ruby
class Image
  def initialize(pixels)
    @pixels = pixels
  end

  def apply_filter(filter_matrix)
    height = @pixels.length
    width = @pixels[0].length
    result = Array.new(height) { Array.new(width, 0) }

    @pixels.each_with_index do |row, i|
      row.each_with_index do |pixel, j|
        result[i][j] = apply_filter_to_pixel(i, j, filter_matrix)
      end
    end

    Image.new(result)
  end

  private

  def apply_filter_to_pixel(row, col, filter_matrix)
    sum = 0
    filter_matrix.each_with_index do |filter_row, i|
      filter_row.each_with_index do |value, j|
        pixel_row = row + i - 1
        pixel_col = col + j - 1
        next unless pixel_row.between?(0, @pixels.length - 1) &&
                    pixel_col.between?(0, @pixels[0].length - 1)

        sum += @pixels[pixel_row][pixel_col] * value
      end
    end
    [sum.round, 255].min
  end
end

# Usage
image_data = [
  [100, 150, 200],
  [50, 75, 100],
  [25, 50, 75]
]

blur_filter = [
  [0.0625, 0.125, 0.0625],
  [0.125, 0.25, 0.125],
  [0.0625, 0.125, 0.0625]
]

image = Image.new(image_data)
blurred_image = image.apply_filter(blur_filter)
```

These examples demonstrate the versatility and power of arrays in real-world scenarios, from data processing to image manipulation. The use of multi-dimensional arrays, various array methods, and custom classes showcases how arrays can be leveraged to solve complex problems efficiently.