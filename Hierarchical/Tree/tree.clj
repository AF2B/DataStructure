(ns tree
  (:require [clojure.spec.alpha :as s]))

(s/def ::value any?)
(s/def ::children (s/coll-of ::tree :kind vector?))
(s/def ::tree (s/keys :req-un [::value]
                      :opt-un [::children]))

(defprotocol TreeOperations
  "Protocolo definindo operações fundamentais da árvore"
  (add-child [this child-value] "Adiciona um novo nó filho com o valor especificado")
  (remove-child [this index] "Remove o filho no índice especificado")
  (update-node-value [this new-value] "Atualiza o valor do nó atual")
  (get-child-at [this index] "Obtém o nó filho no índice especificado")
  (get-tree-depth [this] "Calcula a profundidade da árvore")
  (get-tree-size [this] "Calcula o número total de nós na árvore"))

(defrecord TreeNode [value children]
  TreeOperations
  (add-child [this child-value]
    (let [new-child (->TreeNode child-value [])]
      (if (s/valid? ::tree new-child)
        (update this :children (fnil conj []) new-child)
        (throw (ex-info "Invalid child node" 
                       {:value child-value
                        :spec-error (s/explain-str ::tree new-child)})))))
  
  (remove-child [this index]
    (if (and (>= index 0) (< index (count (:children this))))
      (update this :children #(vec (concat (subvec % 0 index)
                                         (subvec % (inc index)))))
      this))
  
  (update-node-value [this new-value]
    (let [updated (assoc this :value new-value)]
      (if (s/valid? ::tree updated)
        updated
        (throw (ex-info "Invalid node value" 
                       {:value new-value
                        :spec-error (s/explain-str ::tree updated)})))))
  
  (get-child-at [this index]
    (get-in this [:children index]))
  
  (get-tree-depth [this]
    (if (empty? (:children this))
      1
      (inc (apply max (map get-tree-depth (:children this))))))
  
  (get-tree-size [this]
    (inc (reduce + 0 (map get-tree-size (:children this))))))

(defn create-tree
  "Cria uma nova árvore com o valor especificado"
  [value]
  (let [tree (->TreeNode value [])]
    (if (s/valid? ::tree tree)
      tree
      (throw (ex-info "Invalid tree structure" 
                     {:value value
                      :spec-error (s/explain-str ::tree tree)})))))

(defn pre-order
  "Realiza travessia pré-ordem na árvore"
  [tree]
  (when (s/valid? ::tree tree)
    (cons (:value tree)
          (mapcat pre-order (:children tree)))))

(defn post-order
  "Realiza travessia pós-ordem na árvore"
  [tree]
  (when (s/valid? ::tree tree)
    (concat (mapcat post-order (:children tree))
            [(:value tree)])))

(defn breadth-first
  "Realiza travessia em largura na árvore"
  [tree]
  (when (s/valid? ::tree tree)
    (loop [queue [tree]
           result []]
      (if (empty? queue)
        result
        (let [current (first queue)]
          (recur (concat (rest queue) (:children current))
                 (conj result (:value current))))))))

(defn find-node
  "Encontra um nó na árvore que satisfaça o predicado"
  [tree pred]
  (when (s/valid? ::tree tree)
    (cond
      (pred tree) tree
      (empty? (:children tree)) nil
      :else (some #(find-node % pred) (:children tree)))))

(defn map-tree
  "Aplica a função f para cada valor de nó na árvore"
  [f tree]
  (when (s/valid? ::tree tree)
    (->TreeNode (f (:value tree))
                (mapv #(map-tree f %) (:children tree)))))

(comment
  (def sample-tree
    (-> (create-tree 1)
        (add-child 2)
        (add-child 3)
        (add-child 4)))
  
  (def nested-tree
    (-> sample-tree
        (add-child 5)
        (add-child 6)))
  
  (pre-order nested-tree)
  ;; => (1 2 3 4 5 6)
  
  (post-order nested-tree)
  ;; => (2 3 4 5 6 1)
  
  (breadth-first nested-tree)
  ;; => [1 2 3 4 5 6]
  
  (get-tree-depth nested-tree)  ;; => 2
  (get-tree-size nested-tree)   ;; => 6
  
  (map-tree #(* 2 %) nested-tree)
  ;; => TreeNode com valores duplicados
  
  (find-node nested-tree #(= 3 (:value %)))
  ;; => TreeNode{:value 3, :children []}
)