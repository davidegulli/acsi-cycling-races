import React from 'react';

import PaginationItemsCount from './pagination-items-count';
import { JhiPagination } from 'react-jhipster';
import { Row } from 'reactstrap';

export interface IPagination {
  activePage: number;
  totalItems: number;
  itemsPerPage: number;
  handlePagination: any;
}

const pagination = (props: IPagination) => {
  const { activePage, totalItems, itemsPerPage, handlePagination } = props;
  return (
    <Row className="pagination-row">
      <span className="pagination-controls align-middle">
        <div className="mt-2 float-left">
          <PaginationItemsCount page={activePage} total={totalItems} itemsPerPage={itemsPerPage} />
        </div>
        <div className="float-right">
          <JhiPagination
            activePage={activePage}
            onSelect={handlePagination}
            maxButtons={5}
            itemsPerPage={itemsPerPage}
            totalItems={props.totalItems}
          />
        </div>
      </span>
    </Row>
  );
};

export default pagination;
