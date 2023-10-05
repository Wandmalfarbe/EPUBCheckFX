package de.pascalwagler.epubcheckfx.ui;

import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;

import java.util.List;
import java.util.function.Function;

import static java.util.stream.Collectors.toList;

/**
 * From https://stackoverflow.com/a/43914715
 */
public class BindingUtil {

    public static <E, F> void mapContent(ObservableList<F> mapped, ObservableList<? extends E> source,
            Function<? super E, ? extends F> mapper) {
        map(mapped, source, mapper);
    }

    private static <E, F> Object map(ObservableList<F> mapped, ObservableList<? extends E> source,
            Function<? super E, ? extends F> mapper) {
        final ListContentMapping<E, F> contentMapping = new ListContentMapping<>(mapped, mapper);
        mapped.setAll(source.stream().map(mapper).collect(toList()));
        source.removeListener(contentMapping);
        source.addListener(contentMapping);
        return contentMapping;
    }

    private static class ListContentMapping<E, F> implements ListChangeListener<E> {
        private final List<F> mapped;
        private final Function<? super E, ? extends F> mapper;

        public ListContentMapping(List<F> mapped, Function<? super E, ? extends F> mapper) {
            this.mapped = mapped;
            this.mapper = mapper;
        }

        @Override
        public void onChanged(Change<? extends E> change) {

            while (change.next()) {
                if (change.wasPermutated()) {
                    mapped.subList(change.getFrom(), change.getTo()).clear();
                    mapped.addAll(change.getFrom(), change.getList().subList(change.getFrom(), change.getTo())
                            .stream().map(mapper).collect(toList()));
                } else {
                    if (change.wasRemoved()) {
                        mapped.subList(change.getFrom(), change.getFrom() + change.getRemovedSize()).clear();
                    }
                    if (change.wasAdded()) {
                        mapped.addAll(change.getFrom(), change.getAddedSubList()
                                .stream().map(mapper).collect(toList()));
                    }
                }
            }

        }

        @Override
        public int hashCode() {
            final List<F> list = mapped;
            return (list == null) ? 0 : list.hashCode();
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }

            final List<F> mapped1 = mapped;
            if (mapped1 == null) {
                return false;
            }

            if (obj instanceof ListContentMapping) {
                final ListContentMapping<?, ?> other = (ListContentMapping<?, ?>) obj;
                final List<?> mapped2 = other.mapped;
                return mapped1 == mapped2;
            }
            return false;
        }
    }
}
