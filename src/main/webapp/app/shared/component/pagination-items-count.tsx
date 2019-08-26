import React from 'react';

export interface IPaginationItmesCount {
  page: number;
  itemsPerPage: number;
  total: number;
}

const paginationItemsCount = (props: IPaginationItmesCount) => {
  const { page, total, itemsPerPage } = props;

  return (
    <span class="pagination-item-count">
      Stai vedendo pagina {(page - 1) * itemsPerPage === 0 ? 1 : (page - 1) * itemsPerPage + 1} -{' '}
      {page * itemsPerPage < total ? page * itemsPerPage : total} di {total} elementi totali
    </span>
  );
};

export default paginationItemsCount;
