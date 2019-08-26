import axios from 'axios';
import { ICrudSearchAction, ICrudGetAction, ICrudGetAllAction, ICrudPutAction, ICrudDeleteAction } from 'react-jhipster';

import { cleanEntity } from 'app/shared/util/entity-utils';
import { REQUEST, SUCCESS, FAILURE } from 'app/shared/reducers/action-type.util';

import { IAthleteBlackList, defaultValue } from 'app/shared/model/athlete-black-list.model';

export const ACTION_TYPES = {
  SEARCH_ATHLETEBLACKLISTS: 'athleteBlackList/SEARCH_ATHLETEBLACKLISTS',
  FETCH_ATHLETEBLACKLIST_LIST: 'athleteBlackList/FETCH_ATHLETEBLACKLIST_LIST',
  FETCH_ATHLETEBLACKLIST: 'athleteBlackList/FETCH_ATHLETEBLACKLIST',
  CREATE_ATHLETEBLACKLIST: 'athleteBlackList/CREATE_ATHLETEBLACKLIST',
  UPDATE_ATHLETEBLACKLIST: 'athleteBlackList/UPDATE_ATHLETEBLACKLIST',
  DELETE_ATHLETEBLACKLIST: 'athleteBlackList/DELETE_ATHLETEBLACKLIST',
  RESET: 'athleteBlackList/RESET'
};

const initialState = {
  loading: false,
  errorMessage: null,
  entities: [] as ReadonlyArray<IAthleteBlackList>,
  entity: defaultValue,
  updating: false,
  totalItems: 0,
  updateSuccess: false
};

export type AthleteBlackListState = Readonly<typeof initialState>;

// Reducer

export default (state: AthleteBlackListState = initialState, action): AthleteBlackListState => {
  switch (action.type) {
    case REQUEST(ACTION_TYPES.SEARCH_ATHLETEBLACKLISTS):
    case REQUEST(ACTION_TYPES.FETCH_ATHLETEBLACKLIST_LIST):
    case REQUEST(ACTION_TYPES.FETCH_ATHLETEBLACKLIST):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        loading: true
      };
    case REQUEST(ACTION_TYPES.CREATE_ATHLETEBLACKLIST):
    case REQUEST(ACTION_TYPES.UPDATE_ATHLETEBLACKLIST):
    case REQUEST(ACTION_TYPES.DELETE_ATHLETEBLACKLIST):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        updating: true
      };
    case FAILURE(ACTION_TYPES.SEARCH_ATHLETEBLACKLISTS):
    case FAILURE(ACTION_TYPES.FETCH_ATHLETEBLACKLIST_LIST):
    case FAILURE(ACTION_TYPES.FETCH_ATHLETEBLACKLIST):
    case FAILURE(ACTION_TYPES.CREATE_ATHLETEBLACKLIST):
    case FAILURE(ACTION_TYPES.UPDATE_ATHLETEBLACKLIST):
    case FAILURE(ACTION_TYPES.DELETE_ATHLETEBLACKLIST):
      return {
        ...state,
        loading: false,
        updating: false,
        updateSuccess: false,
        errorMessage: action.payload
      };
    case SUCCESS(ACTION_TYPES.SEARCH_ATHLETEBLACKLISTS):
    case SUCCESS(ACTION_TYPES.FETCH_ATHLETEBLACKLIST_LIST):
      return {
        ...state,
        loading: false,
        entities: action.payload.data,
        totalItems: parseInt(action.payload.headers['x-total-count'], 10)
      };
    case SUCCESS(ACTION_TYPES.FETCH_ATHLETEBLACKLIST):
      return {
        ...state,
        loading: false,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.CREATE_ATHLETEBLACKLIST):
    case SUCCESS(ACTION_TYPES.UPDATE_ATHLETEBLACKLIST):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.DELETE_ATHLETEBLACKLIST):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: {}
      };
    case ACTION_TYPES.RESET:
      return {
        ...initialState
      };
    default:
      return state;
  }
};

const apiUrl = 'api/athlete-black-lists';
const apiSearchUrl = 'api/_search/athlete-black-lists';

// Actions

export const getSearchEntities: ICrudSearchAction<IAthleteBlackList> = (query, page, size, sort) => ({
  type: ACTION_TYPES.SEARCH_ATHLETEBLACKLISTS,
  payload: axios.get<IAthleteBlackList>(`${apiSearchUrl}?query=${query}${sort ? `&page=${page}&size=${size}&sort=${sort}` : ''}`)
});

export const getEntities: ICrudGetAllAction<IAthleteBlackList> = (page, size, sort) => {
  const requestUrl = `${apiUrl}${sort ? `?page=${page}&size=${size}&sort=${sort}` : ''}`;
  return {
    type: ACTION_TYPES.FETCH_ATHLETEBLACKLIST_LIST,
    payload: axios.get<IAthleteBlackList>(`${requestUrl}${sort ? '&' : '?'}cacheBuster=${new Date().getTime()}`)
  };
};

export const getEntity: ICrudGetAction<IAthleteBlackList> = id => {
  const requestUrl = `${apiUrl}/${id}`;
  return {
    type: ACTION_TYPES.FETCH_ATHLETEBLACKLIST,
    payload: axios.get<IAthleteBlackList>(requestUrl)
  };
};

export const createEntity: ICrudPutAction<IAthleteBlackList> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.CREATE_ATHLETEBLACKLIST,
    payload: axios.post(apiUrl, cleanEntity(entity))
  });
  dispatch(getEntities());
  return result;
};

export const updateEntity: ICrudPutAction<IAthleteBlackList> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.UPDATE_ATHLETEBLACKLIST,
    payload: axios.put(apiUrl, cleanEntity(entity))
  });
  dispatch(getEntities());
  return result;
};

export const deleteEntity: ICrudDeleteAction<IAthleteBlackList> = id => async dispatch => {
  const requestUrl = `${apiUrl}/${id}`;
  const result = await dispatch({
    type: ACTION_TYPES.DELETE_ATHLETEBLACKLIST,
    payload: axios.delete(requestUrl)
  });
  dispatch(getEntities());
  return result;
};

export const reset = () => ({
  type: ACTION_TYPES.RESET
});
